package com.qzj.logistics.view.books.adapter;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qzj.logistics.R;
import com.qzj.logistics.view.books.widget.ContactItemInterface;
import com.qzj.logistics.view.books.widget.ContactListAdapter;

public class BookAdapter extends ContactListAdapter {

    public BookAdapter(Context _context, int _resource) {
        super(_context, _resource);
    }

    public void populateDataForRow(View parentView, ContactItemInterface item, final int position) {
        View infoView = parentView.findViewById(R.id.infoRowContainer);
        TextView nicknameView = (TextView) infoView.findViewById(R.id.phone_book_nike_name);
        TextView phoneNumberView = (TextView) infoView.findViewById(R.id.phone_book_phone_number);
        RadioButton radioButton = (RadioButton) infoView.findViewById(R.id.phone_book_radio);

        nicknameView.setText(item.getDisplayInfo());
        phoneNumberView.setText(item.getDisplayInfo_PhoneNumber());
        radioButton.setChecked(item.getRbState());
    }

}
