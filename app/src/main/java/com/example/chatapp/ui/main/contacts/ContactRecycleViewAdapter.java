package com.example.chatapp.ui.main.contacts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentContactCardBinding;
import com.example.chatapp.model.UserInfoViewModel;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ContactRecycleViewAdapter extends RecyclerView.Adapter<ContactRecycleViewAdapter.ContactViewHolder>{
    //Store all of the blogs to present
    private final List<ContactCard> mContacts;
    //Store the expanded state for each List item, true -> expanded, false -> not

    private final Map<ContactCard, Boolean> mExpandedFlags;

    private ContactFragment mParentFragment;





    public ContactRecycleViewAdapter(List<ContactCard> items, ContactFragment parentfragment) {

        this.mContacts = items;
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


    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {


        holder.setContacts(mContacts.get(position));


        // sets on click listener for each contact card
/*        holder.binding.cardRoot.setOnClickListener(button -> {
            Navigation.findNavController(holder.mView).navigate(
                    ContactFragmentDirections.actionNavigationConnectionsToContactPageFragment(mContacts.get(position))
            );
            Log.i("Button","Pressed");
        });*/

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


            binding.declineButton.setOnClickListener(button -> {
                Log.i("Button","Decline Pressed");

                mParentFragment.mModel.connectReject(mParentFragment.mUserInfoModel.getMemberID(), mContact.getNick());
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





        private void handleClick(final View theView) {
            Log.i("Button","Whole Card Pressed");
            mExpandedFlags.put(mContact,!mExpandedFlags.get(mContact));
            displayFullContact();
        }

        private void displayFullContact() {
            if (mExpandedFlags.get(mContact) && mContact.getAccepted()) {
                binding.nickName.setVisibility(View.VISIBLE);
                binding.email.setVisibility(View.VISIBLE);
                binding.chatButton.setVisibility(View.VISIBLE);
                binding.declineButton.setVisibility(View.GONE);
                binding.addButton.setVisibility(View.GONE);
            } else if(!mContact.getAccepted()) {
                binding.fullName.setVisibility(View.VISIBLE);
                binding.addButton.setVisibility(View.VISIBLE);
                binding.declineButton.setVisibility(View.VISIBLE);
                binding.email.setVisibility(View.GONE);
                binding.nickName.setVisibility(View.GONE);
                binding.chatButton.setVisibility(View.GONE);
            } else {
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
