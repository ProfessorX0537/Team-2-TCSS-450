package com.example.chatapp.ui.main.contacts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.FragmentContactCardBinding;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ContactRecycleViewAdapter extends RecyclerView.Adapter<ContactRecycleViewAdapter.ContactViewHolder>{
    //Store all of the blogs to present
    private final List<ContactCard> mContacts;
    //Store the expanded state for each List item, true -> expanded, false -> not

    private final Map<ContactCard, Boolean> mExpandedFlags;



    public ContactRecycleViewAdapter(List<ContactCard> items) {
        this.mContacts = items;

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

        }

        private void handleClick(final View theView) {
            mExpandedFlags.put(mContact,!mExpandedFlags.get(mContact));
            displayFullContact();
        }

        private void displayFullContact() {
            if (mExpandedFlags.get(mContact)) {
                binding.nickName.setVisibility(View.VISIBLE);
                binding.email.setVisibility(View.VISIBLE);
                binding.chatButton.setVisibility(View.VISIBLE);
                binding.removeButton.setVisibility(View.VISIBLE);
            } else {

                binding.nickName.setVisibility(View.GONE);
                binding.email.setVisibility(View.GONE);
                binding.chatButton.setVisibility(View.GONE);
                binding.removeButton.setVisibility(View.GONE);
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
