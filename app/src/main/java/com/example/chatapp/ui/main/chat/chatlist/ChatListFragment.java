package com.example.chatapp.ui.main.chat.chatlist;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentChatListBinding;
import com.example.chatapp.model.NewMessageCountViewModel;
import com.example.chatapp.model.UserInfoViewModel;
import com.example.chatapp.ui.main.chat.chatroom.ChatRoomItemsViewModel;
import com.example.chatapp.ui.main.chat.chatroom.add.ChatRoomAddUserRequestsViewModel;
import com.example.chatapp.ui.main.home.HomeMessagesItemViewModel;

import java.util.ArrayList;

public class ChatListFragment extends Fragment {
    private UserInfoViewModel userinfo;
    private ChatListItemViewModel mItemModel;
    private ChatListAddViewModel mAddModel;
    private FragmentChatListBinding mBinding;
    private ChatRoomAddUserRequestsViewModel mDeleteModel;
    private NewMessageCountViewModel mNewMessageCountModel;

    private HomeMessagesItemViewModel mHomeMessagesItemViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ViewModels
        userinfo = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mItemModel = new ViewModelProvider(getActivity()).get(ChatListItemViewModel.class);

        mAddModel = new ViewModelProvider(getActivity()).get(ChatListAddViewModel.class);

        mDeleteModel = new ViewModelProvider(getActivity()).get(ChatRoomAddUserRequestsViewModel.class);

        mNewMessageCountModel = new ViewModelProvider(getActivity()).get(NewMessageCountViewModel.class);

        mHomeMessagesItemViewModel = new ViewModelProvider(getActivity()).get(HomeMessagesItemViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentChatListBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //mModel.mItemList observe
        mItemModel.addItemListObserver(getViewLifecycleOwner(), list -> {
            if (mBinding.rootRecycler.getAdapter() == null) {
                mBinding.rootRecycler.setAdapter(new ChatListAdapter(list, getActivity(), mHomeMessagesItemViewModel.mChatRoomIdNavigate, getView()));
                mHomeMessagesItemViewModel.mChatRoomIdNavigate = -1;
            } else {
//                ((ChatListAdapter)mBinding.rootRecycler.getAdapter()).
                ((ChatListAdapter)mBinding.rootRecycler.getAdapter()).mChatListItems = list;
                mBinding.rootRecycler.getAdapter().notifyDataSetChanged();

//                for (int i = 0; i < list.size(); i++) {
//                    mBinding.rootRecycler.getAdapter().notifyItemChanged(i);
//                    mBinding.rootRecycler.getAdapter().notifyItemInserted(i);
//                }
            }

            Log.v("ChatListFragment", "Observed ItemModel Response create new!");
//            if (mBinding.rootRecycler.getAdapter() == null) { //
//                mBinding.rootRecycler.setAdapter(new ChatListAdapter(mItemModel.mItemList.getValue()));
//                Log.v("ChatListFragment", "Observed ItemModel Response create new!");
//            } else {
//                mBinding.rootRecycler.getAdapter().notifyDataSetChanged();
//                Log.v("ChatListFragment", "Observed ItemModel Response notify!");
//            }
            showSpinner(false);

            //get notifications hashmap and set according
            ArrayList<ChatListItem> list1 = ((ChatListAdapter) mBinding.rootRecycler.getAdapter()).mChatListItems;
            for (int i = 0; i < list1.size(); i++) {
                ChatListItem curr = list1.get(i);

                if(!mNewMessageCountModel.mNewHashMapMessageCount.getValue().containsKey(curr.mRoomID)) continue;
                curr.mNotifCount = mNewMessageCountModel.mNewHashMapMessageCount.getValue().get(curr.mRoomID);
                mBinding.rootRecycler.getAdapter().notifyItemChanged(i);
            }
        });
        mItemModel.getChatRooms(userinfo.getMemberID(), userinfo.getJwt());

        //Observe remove request
        mDeleteModel.addRemoveSelfFromChatResponseObserver(getViewLifecycleOwner(), jsonObject -> {
            if (jsonObject == null) return;
            mItemModel.getChatRooms(userinfo.getMemberID(), userinfo.getJwt()); //refresh
            mDeleteModel.clearResponses();
        });

        //scrolling
        mBinding.rootRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        mBinding.floatingActionButton.setVisibility(View.VISIBLE);
//                        mBinding.floatingActionButton.animate().alpha(1f).setDuration(1).setListener(null);
                        break;
                    default:
                        mBinding.floatingActionButton.setVisibility(View.GONE);
//                        mBinding.floatingActionButton.animate().alpha(0f).setDuration(0).setListener(null);
                }
            }
        });

        mBinding.rootRecycler.addOnItemTouchListener(
                new RecyclerTouchListener(this.getContext(), mBinding.rootRecycler, new ClickListener() {
                    @Override
                    public void onLongClick(View view, int position) {
                        Log.v("long click", "Long clicked a chat room");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                                .setPositiveButton(R.string.alert_pos_chatroom_do_leave, (dialog, which) -> {
                                    mDeleteModel.requestRemoveSelfFromChat(mItemModel.mItemList.getValue().get(position).getmRoomID(), userinfo);
                                })
                                .setNegativeButton(R.string.alert_neg_chatroom_do_leave, (dialog, which) -> {
                                    dialog.cancel();
                                });
                        alertDialogBuilder.setTitle(R.string.alert_title_chatroom_do_leave);
                        alertDialogBuilder.setMessage(R.string.alert_msg_chatroom_do_leave);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }));

        //Add Floating Button
        mBinding.floatingActionButton.setOnClickListener(button -> {
            //https://www.geeksforgeeks.org/how-to-create-a-custom-alertdialog-in-android/
            // Create an alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.title_chatlist_create_new);

            // set the custom layout
            final View customLayout = getLayoutInflater().inflate(R.layout.dialog_generic_edit_text, null);
            builder.setView(customLayout);

            //edit text hint
            ((EditText)customLayout.findViewById(R.id.edit_text_generic)).setHint(R.string.alert_et_hint_chatroom_do_renaming);

            // add a button
            builder.setPositiveButton(R.string.button_chatlist_create_pos, (dialog, which) -> {
                // send data from the AlertDialog to the Activity
                EditText editText = customLayout.findViewById(R.id.edit_text_generic);
                mAddModel.requestNewChatRoom(editText.getText().toString().trim(), userinfo.getMemberID(), userinfo.getJwt());
                showSpinner(true);
            });
            builder.setNegativeButton(R.string.button_chatlist_create_neg, (dialog, which) -> {
                dialog.cancel();
            });
            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        //Observe mAddModel mResponse & refresh recycler view
        mAddModel.addResponseObserver(
                getViewLifecycleOwner(),
                json -> {
//                    mBinding.rootRecycler.getAdapter().notifyDataSetChanged();
//                    showSpinner(false);
                    mItemModel.getChatRooms(userinfo.getMemberID(), userinfo.getJwt());
                    Log.v("ChatListFragment", "Observed AddModel Response notify!");
                }
        );
//        showSpinner(true); //init show progress spinner

        //observe mMessageCountModel
        mNewMessageCountModel.addNewHashMapMessageCountObserver(getViewLifecycleOwner(), hashmap -> {
            mItemModel.getChatRooms(userinfo.getMemberID(), userinfo.getJwt());
        });
    }

    private void showSpinner(boolean show) {
        mBinding.progressBar3.setVisibility(show ? View.VISIBLE : View.GONE);
        mBinding.floatingActionButton.setVisibility(!show ? View.VISIBLE : View.GONE);
        mBinding.rootRecycler.setVisibility(!show ? View.VISIBLE : View.GONE);
    }

    /**
     * This inner class allows us to detect long clicks on
     * Comes from <a href="https://medium.com/@harivigneshjayapalan/android-recyclerview-implementing-single-item-click-and-long-press-part-ii-b43ef8cb6ad8">...</a>
     */
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onLongClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        public void onLongClick(View view, int position);
    }

//    public void updateChatMessage() {
//
//    }

    @Override
    public void onResume() {
        super.onResume();
        //reset chatId
        ChatRoomItemsViewModel tempVM = new ViewModelProvider(getActivity()).get(ChatRoomItemsViewModel.class);
        tempVM.mChatId = -1;
    }
}