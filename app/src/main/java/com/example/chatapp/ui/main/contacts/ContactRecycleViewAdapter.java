package com.example.chatapp.ui.main.contacts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentContactCardBinding;
import com.example.chatapp.model.UserInfoViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ContactRecycleViewAdapter extends RecyclerView.Adapter<ContactRecycleViewAdapter.ContactViewHolder> implements Filterable {
    //Store all of the blogs to present
    private final List<ContactCard> mContacts;
    private final List<ContactCard> mContactsFull;
    //Store the expanded state for each List item, true -> expanded, false -> not

    private final Map<ContactCard, Boolean> mExpandedFlags;

    private ContactFragment mParentFragment;





    public ContactRecycleViewAdapter(List<ContactCard> items, ContactFragment parentfragment) {

        this.mContacts = items;
        this.mContactsFull = new ArrayList<>(items);
        this.mParentFragment = parentfragment;



        mExpandedFlags = mContacts.stream()
                .collect(Collectors.toMap(Function.identity(), contact -> false));


    }



    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflates contact card for recycler view
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false));

    }



    // Filter for search bar

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<ContactCard> filteredList = new ArrayList<>();
                if(charSequence == null || charSequence.length() == 0){
                    filteredList.addAll(mContactsFull);
                }else{
                    String filterPattern = charSequence.toString().toLowerCase().trim();
                    for(ContactCard contact : mContactsFull){
                        // search by name, email, or nickname
                        if(contact.getNick().toLowerCase().contains(filterPattern)
                                || contact.getName().toLowerCase().contains(filterPattern)
                                || contact.getEmail().toLowerCase().contains(filterPattern)){
                            filteredList.add(contact);
                        }
                    }
                }
                // return filtered list
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;


            }




            // updates recycler view with filtered list
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mContacts.clear();
                mContacts.addAll((List) filterResults.values);
                notifyDataSetChanged();

            }
        };

    }




    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {


        holder.setContacts(mContacts.get(position));


        // sets on click listener for each contact card


    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentContactCardBinding binding;

        private ContactCard mContact;



        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentContactCardBinding.bind(view);
            binding.cardRoot.setOnClickListener(this::handleClick);
            binding.addButton.setOnClickListener(button -> {
                mContact.setAccepted(true);
                mParentFragment.mModel.connectAccept(mParentFragment.mUserInfoModel.getMemberID(), mContact.getMemberID());
                displayFullContact();
            });


            // decline button that rejects contact request and removes contact from contact view model
            binding.declineButton.setOnClickListener(button -> {
                Log.i("Button","Decline Pressed");

                mParentFragment.mModel.connectReject(mParentFragment.mUserInfoModel.getMemberID(), mContact.getMemberID());


                mContacts.remove(mContact);
                notifyDataSetChanged();
                displayFullContact();
            });



        }

/*        private void handleAdd(final View theView) {

            System.out.println("Add");


            Log.i("Button","Add Pressed");
            mParentFragment.mModel.connectAccept(mParentFragment.mUserInfoModel.getMemberID(), mContact.getMemberID());
            mContact.setAccepted(true);

            displayFullContact();
        }

        private void handleDecline(final View theView) {

            System.out.println("Decline");
            Log.i("Button","Decline Pressed");
            mParentFragment.mModel.connectReject(mParentFragment.mUserInfoModel.getMemberID(), mContact.getNick());
            mContacts.remove(mContact);
            notifyDataSetChanged();
            displayFullContact();
        }*/





        // expands contact card when clicked
        private void handleClick(final View theView) {
            Log.i("Button","Whole Card Pressed");
            mExpandedFlags.put(mContact,!mExpandedFlags.get(mContact));
            displayFullContact();
        }

        // functions that sets visibility of certain elements in contact card based off what type of contact card it is
        private void displayFullContact() {
            if (mExpandedFlags.get(mContact) && mContact.getAccepted()) {
                binding.nickName.setVisibility(View.VISIBLE);
                binding.email.setVisibility(View.VISIBLE);
                binding.chatButton.setVisibility(View.VISIBLE);
                binding.declineButton.setVisibility(View.GONE);
                binding.addButton.setVisibility(View.GONE);
            } else if(!mContact.getAccepted() && mContact.getIncoming()){
                binding.fullName.setVisibility(View.VISIBLE);
                binding.addButton.setVisibility(View.VISIBLE);
                binding.declineButton.setVisibility(View.VISIBLE);
                binding.email.setVisibility(View.GONE);
                binding.nickName.setVisibility(View.GONE);
                binding.chatButton.setVisibility(View.GONE);
            } else if(!mContact.getAccepted() && mContact.getOutgoing()){
                binding.fullName.setVisibility(View.VISIBLE);
                binding.addButton.setVisibility(View.GONE);
                binding.declineButton.setVisibility(View.VISIBLE);
                binding.email.setVisibility(View.GONE);
                binding.nickName.setVisibility(View.GONE);
                binding.chatButton.setVisibility(View.GONE);

            }else {
                    binding.declineButton.setVisibility(View.GONE);
                    binding.addButton.setVisibility(View.GONE);
                    binding.nickName.setVisibility(View.GONE);
                    binding.email.setVisibility(View.GONE);
                    binding.chatButton.setVisibility(View.GONE);
            }
        }


        void setContacts(final ContactCard contact) {
            mContact = contact;
            binding.fullName.setText(contact.getName());
            binding.nickName.setText(contact.getNick());
            binding.email.setText(contact.getEmail());

            displayFullContact();

        }





    }
}
