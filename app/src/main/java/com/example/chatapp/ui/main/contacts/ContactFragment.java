package com.example.chatapp.ui.main.contacts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentContactsBinding;
import com.example.chatapp.model.UserInfoViewModel;
import com.example.chatapp.ui.main.chat.chatlist.ChatListFragment;

public class ContactFragment extends Fragment {

    ContactsViewModel mModel;

    UserInfoViewModel mUserInfoModel;

    FragmentContactsBinding mBinding;




//    private ContactsViewModel mModel;
    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());

        mModel = provider.get(ContactsViewModel.class);
        mUserInfoModel = provider.get(UserInfoViewModel.class);

        updateList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
//        if (view instanceof RecyclerView) {
//            ((RecyclerView) view).setAdapter(
//                    new ContactRecycleViewAdapter(ContactGenerator.getCardList()));
//        }
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //hides floating button on scroll
        mBinding = FragmentContactsBinding.bind(getView());
        mBinding.addContactFab.setOnClickListener(this::requestConnection);





        //When user adds a contact, the list is updated
        mModel.mAddedContactResponse.observe(getViewLifecycleOwner(), response -> {
            if (response != null) {
                mModel.connectGet(mUserInfoModel.getMemberID());
            }
        });

        //Adds recycle view adapter to the list
        mModel.addContactsObserver(getViewLifecycleOwner(), contactsList -> {


            if (!contactsList.isEmpty()) {
                mBinding.listRoot.setAdapter(
                        new ContactRecycleViewAdapter(contactsList, this)
                );
                mBinding.listRoot.getAdapter().notifyDataSetChanged();
            }
        });






        // scroll listener to make floating button disappear
        mBinding.listRoot.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        mBinding.addContactFab.setVisibility(View.VISIBLE);
                        break;
                    default:
                        mBinding.addContactFab.setVisibility(View.GONE);
                }
            }
        });

        // search bar listener with filter
        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ContactRecycleViewAdapter adapter = (ContactRecycleViewAdapter) mBinding.listRoot.getAdapter();
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });



        // long click listerner to delete connection

        mBinding.listRoot.addOnItemTouchListener(
                new ChatListFragment.RecyclerTouchListener(this.getContext(), mBinding.listRoot, new ChatListFragment.ClickListener() {
                    @Override
                    public void onLongClick(View view, int position) {
                        if(!mModel.getContacts().get(position).getAccepted()){
                            return;
                        }
                        Log.v("long click","Long clicked a connection");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext())
                                .setPositiveButton(R.string.alert_action_yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //delete request and remove from contact view model

                                        mModel.connectDelete(mUserInfoModel.getMemberID(), mModel.getContacts().get(position).getMemberID());
                                        mModel.removeContact(position);

                                        mBinding.listRoot.getAdapter().notifyItemRemoved(position);
                                        Log.v("Delete","connection should get deleted");


                                    }
                                })
                                .setNegativeButton(R.string.alert_action_no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        alertDialogBuilder.setTitle(R.string.alert_title_delete_connection);
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                    }
                }));


        // checks if user is scrolling up or down and hides search bar accordingly
        mBinding.listRoot.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                int firstVisibleItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                Log.i("Scroll Y", String.valueOf(firstVisibleItem));

                if(lastFirstVisibleItem<firstVisibleItem){

                    mBinding.searchView.setVisibility(View.GONE);
                    Log.d("RecyclerView scrolled: ", "up!");

                } else if (lastFirstVisibleItem>firstVisibleItem){
                        mBinding.searchView.setVisibility(View.VISIBLE);

                        Log.d("RecyclerView scrolled: ", "down!");
                }


                lastFirstVisibleItem = firstVisibleItem;
            }


        });

//        mBinding.listRoot.setAdapter(new ContactRecycleViewAdapter(ContactGenerator.getCardList()));


    }
    public void updateList(){
        mModel.connectGet(mUserInfoModel.getMemberID());
    }





    //TODO: change to recycler view type .
    private void requestConnection(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(R.string.textview_connections_request);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_addconnection, null);
        alertDialogBuilder.setView(dialogView)
                .setPositiveButton(R.string.action_connections_Add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: add connection request stuff here.


                        EditText edit = (EditText) dialogView.findViewById(R.id.edit_text_name);
                        if(edit == null){
                            Log.v("Add","edit text is null");
                            return;
                        }
                        String text=edit.getText().toString();
                        mModel.connectAdd(mUserInfoModel.getMemberID(), text);

                        if (mBinding.listRoot.getAdapter() != null) {
                            mBinding.listRoot.getAdapter().notifyDataSetChanged();
                        }

                        Log.v("Add","connection should get added");

                    }
                })
                .setNegativeButton(R.string.action_connections_cancel, new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    /**
     * This inner class allows us to detect long clicks on Recycler view items
     * Comes from <a href="https://medium.com/@harivigneshjayapalan/android-recyclerview-implementing-single-item-click-and-long-press-part-ii-b43ef8cb6ad8">...</a>
     */
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ChatListFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ChatListFragment.ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onLongClick(child,rv.getChildAdapterPosition(child));
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



    public static interface ClickListener{
        public void onLongClick(View view,int position);
    }
}