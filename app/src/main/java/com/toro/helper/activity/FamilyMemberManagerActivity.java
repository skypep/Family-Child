package com.toro.helper.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toro.helper.R;
import com.toro.helper.base.ToroActivity;
import com.toro.helper.fragment.family.FamilyMemberAdapter;
import com.toro.helper.modle.FamilyUserInfo;
import com.toro.helper.modle.data.FamilyMemberData;
import com.toro.helper.modle.data.ToroDataModle;
import com.toro.helper.modle.data.listener.FamilyMemberDataOnChangeListener;
import com.toro.helper.utils.ConnectManager;
import com.toro.helper.view.AutoLoadRecyclerView;
import com.toro.helper.view.MainActionBar;
import com.toro.helper.view.RecyclerItemDecoration;
import com.toro.helper.view.iphone.IphoneDialogBottomMenu;
import com.toro.helper.view.iphone.MenuItemOnClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Create By liujia
 * on 2018/10/31.
 **/
public class FamilyMemberManagerActivity extends ToroActivity implements FamilyMemberDataOnChangeListener {

    private static final int CONTACT_REQUEST_CODE = 0x0015;

    private FamilyMemberAdapter adapter;

    protected AutoLoadRecyclerView recyclerView;
    private TextView emptyHint;
    private ProgressBar loadingProgress;
    private FamilyMemberData memberData;
    private MainActionBar mainActionBar;
    private boolean[] deleteChecks;
    private LinearLayout deleteLayout;
    private boolean editMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_member_manager_activity);
        initView();
        setNormalAction();
    }

    @Override
    public void onResume() {
        super.onResume();
        ToroDataModle.getInstance().addToroDataModeOnChangeListener(this);
        updateMemberList();
    }

    @Override
    public void onPause() {
        super.onPause();
        ToroDataModle.getInstance().removeToroDataModeOnChangeListener(this);
    }

    protected void initView() {
        adapter = new FamilyMemberAdapter(this,null);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerItemDecoration(getResources().getDimensionPixelOffset(R.dimen.photo_list_photo_offset)));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnPauseListenerParams(new AutoLoadRecyclerView.OnLoadImageSwitchListener() {
            @Override
            public void onLoadImageSwitch(boolean flag) {
                adapter.onLoadImageSwitch(flag);
            }
        },false,false);
        adapter.setAgreenListener(agreenListener);
        emptyHint = findViewById(R.id.empty_hint);
        loadingProgress = findViewById(R.id.loading_progress);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        deleteLayout = findViewById(R.id.delete_layout);
    }

    private void setNormalAction() {
        mainActionBar = findViewById(R.id.main_action_view);
        mainActionBar.updateView(getResources().getString(R.string.family_member_manager), R.mipmap.action_back_icon, R.mipmap.icon_action_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> menus = new ArrayList<>();
                menus.add(getString(R.string.add_family_from_contact));
                menus.add(getString(R.string.add_family_from_edit));
                menus.add(getString(R.string.delete));
                IphoneDialogBottomMenu dialog = new IphoneDialogBottomMenu(FamilyMemberManagerActivity.this, menus, new MenuItemOnClickListener() {
                    @Override
                    public void onClickMenuItem(View v, int item_index, String item) {
                        if (item.equals(getString(R.string.add_family_from_contact))) {
                            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                            startActivityForResult(intent, CONTACT_REQUEST_CODE);
                        } else if (item.equals(getString(R.string.add_family_from_edit))) {
                            startActivity(FamilyMemberEditActivity.createAddIntent(FamilyMemberManagerActivity.this, null));
                        } else if(item.equals(getString(R.string.delete))) {
                            enterEditMode();
                        }
                    }
                });
                dialog.show();
            }
        });
        mainActionBar.removeRightText();
    }

    private void enterEditMode() {
        editMode = true;
        mainActionBar.removeAddRightImage();
        mainActionBar.updateView(getResources().getString(R.string.edit), 0, 0, null, null);
        mainActionBar.addRightText(getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitEditMode();
            }
        });
        adapter.enterEditMode(getDeleteChecks(memberData.getFamilyMemberDatas().size()),onCheckClickListener);
    }

    private void exitEditMode() {
        editMode = false;
        setNormalAction();
        adapter.exitEditMode();
        deleteLayout.setVisibility(View.GONE);
        deleteChecks = null;
    }

    @Override
    public void onBackPressed() {
        if(editMode) {
            exitEditMode();
        }else {
            super.onBackPressed();
        }
    }

    private boolean[] getDeleteChecks(int size) {
        if(deleteChecks == null) {
            deleteChecks = new boolean[size];
        } else {
            boolean [] temp = deleteChecks;
            deleteChecks = new boolean[size];
            int count = temp.length >= deleteChecks.length ? deleteChecks.length:temp.length;
            for(int i = 0; i < count; i++) {
                deleteChecks[i] = temp[i];
            }
        }
        return deleteChecks;
    }

    private void changeDeleteLayout() {
        int deleteCount = 0;
        for(boolean flag : deleteChecks) {
            if(flag) {
                deleteCount ++;
            }
        }
        if(deleteCount > 0) {
            deleteLayout.setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.delete_text)).setText(getString(R.string.delete) + "(" + deleteCount + ")");
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMembers();
                }
            });
        } else {
            deleteLayout.setVisibility(View.GONE);
        }
    }

    private void showProgress() {
        recyclerView.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.VISIBLE);
        emptyHint.setVisibility(View.GONE);
    }

    private void deleteMembers() {
        showProgress();
        List<Integer> ids = new ArrayList<>();
        for(int i=0; i< deleteChecks.length;i++) {
            if(deleteChecks[i]) {
                ids.add(memberData.getFamilyMemberDatas().get(i).getId());
            }
        }
        ConnectManager.getInstance().deleteMemberList(this,ids,ToroDataModle.getInstance().getLocalData().getToken());
    }

    private View.OnClickListener onCheckClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            deleteChecks[index] = !deleteChecks[index];
            adapter.updateEditCheckBox(deleteChecks);
            changeDeleteLayout();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_REQUEST_CODE) {
            String phoneNumber = "";
            if(data != null) {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (null != cursor && cursor.moveToFirst()){
                    phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //得到纯数字电话号码
                    if (phoneNumber.startsWith("+86")) {
                        phoneNumber = phoneNumber.replace("+86", "");
                    }
                    phoneNumber = phoneNumber.replace(" ", "");
                    phoneNumber = phoneNumber.replace("-", "");
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    cursor.close();
                    FamilyUserInfo userInfo = new FamilyUserInfo();
                    userInfo.setPhone(phoneNumber);
                    userInfo.setName(name);
                    startActivity(FamilyMemberEditActivity.createAddIntent(this,phoneNumber,name));
                }
            }
        }
    }

    protected void showEmptyHint() {
        recyclerView.setVisibility(View.GONE);
        loadingProgress.setVisibility(View.GONE);
        emptyHint.setVisibility(View.VISIBLE);
        emptyHint.setText(getString(R.string.empty_family_hint));
    }

    protected void updateMemberList() {
        memberData = ToroDataModle.getInstance().getFamilyMemberData();
        if(memberData == null || memberData.getFamilyMemberDatas() == null || memberData.getFamilyMemberDatas().size() < 1) {
            showEmptyHint();
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            loadingProgress.setVisibility(View.GONE);
            emptyHint.setVisibility(View.GONE);
            if(editMode) {
                adapter.updateEditMode(getDeleteChecks(memberData.getFamilyMemberDatas().size()),memberData.getFamilyMemberDatas());
            } else {
                adapter.updateDatas(memberData.getFamilyMemberDatas());
            }
        }
        recyclerView.setLoadMoreListener(new AutoLoadRecyclerView.onLoadMoreListener() {
            @Override
            public void loadMore() {
                ToroDataModle.getInstance().updateMoreFamilyMemberList();
            }
        });
    }

    private void showMemberList() {
        if(memberData == null || memberData.getFamilyMemberDatas().size() < 1) {
            showEmptyHint();
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            loadingProgress.setVisibility(View.GONE);
            emptyHint.setVisibility(View.GONE);
            adapter.updateDatas(memberData.getFamilyMemberDatas());
        }

    }

    private View.OnClickListener agreenListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            ConnectManager.getInstance().agreenMember(FamilyMemberManagerActivity.this,memberData.getFamilyMemberDatas().get(index).getId(),ToroDataModle.getInstance().getLocalData().getToken());
        }
    };

    @Override
    public void onChange(Object obj) {
        updateMemberList();
    }

    @Override
    public boolean bindData(int tag, Object object) {
        boolean flag = super.bindData(tag, object);
        switch (tag) {
            case ConnectManager.AGREEN_MEMBER:
                ToroDataModle.getInstance().updateToroFamilyMemberList();
                if(flag) {
                    ToroDataModle.getInstance().updateToroFamilyPhotoList();
                }
                break;
            case ConnectManager.DELETE_MEMBER_LIST:
                if(flag) {
                    ToroDataModle.getInstance().updateToroFamilyMemberList();
                    exitEditMode();
//                    updateMemberList();// 可以删除
                } else {
                    showMemberList();
                    Toast.makeText(this,R.string.delete_failed,Toast.LENGTH_LONG).show();
                }

                break;
        }
        return flag;
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,FamilyMemberManagerActivity.class);
        return intent;
    }
}
