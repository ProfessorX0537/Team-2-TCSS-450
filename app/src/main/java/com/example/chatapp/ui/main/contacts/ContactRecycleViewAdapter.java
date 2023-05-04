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

public class ContactRecycleViewAdapter extends RecyclerView.Adapter<ContactRecycleViewAdapter.ContactViewHolder>{
    //Store all of the blogs to present
    private final List<ContactCard> mContacts;
    //Store the expanded state for each List item, true -> expanded, false -> not



    public ContactRecycleViewAdapter(List<ContactCard> items) {
        this.mContacts = items;


    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContacts(mContacts.get(position));

        holder.binding.cardRoot.setOnClickListener(button -> {
            Navigation.findNavController(holder.mView).navigate(
                    ContactFragmentDirections.actionNavigationConnectionsToContactPageFragment()
            );
            Log.i("Button","Pressed");
        });

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


        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentContactCardBinding.bind(view);

        }
        void setContacts(final ContactCard contact) {
            binding.name.setText(contact.getName());

        }



    }
}
