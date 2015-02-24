package com.tnt.scoreboard.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.tnt.scoreboard.R;
import com.tnt.scoreboard.utils.ColorUtils;
import com.tnt.scoreboard.utils.Constants;
import com.tnt.scoreboard.utils.DrawableUtils;

public class NavigationAdapter extends RecyclerView.Adapter<NavigationAdapter.NavigationViewHolder> {

    private static final String DIVIDER = "DIVIDER";
    private final TypedArray mIcons, mColors;
    private final String[] mText;
    private int mCurrentPosition;
    private Person mPerson;
    private String mEmail;
    private IOnNavigationClickListener mListener;

    public NavigationAdapter(Context context) {
        mIcons = context.getResources().obtainTypedArray(R.array.navigation_icons);
        mText = context.getResources().getStringArray(R.array.navigation_items);
        mColors = context.getResources().obtainTypedArray(R.array.navigation_colors);
    }

    @Override
    public NavigationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case Constants.TYPE_HEADER:
                layout = inflater.inflate(R.layout.item_navigation_header, parent, false);
                break;
            case Constants.TYPE_ITEM:
                layout = inflater.inflate(R.layout.item_navigation, parent, false);
                break;
            case Constants.TYPE_DIVIDER:
                layout = inflater.inflate(R.layout.divider_horizontal, parent, false);
                break;
        }
        return new NavigationViewHolder(layout, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? Constants.TYPE_HEADER :
                mText[position - 1].equals(DIVIDER) ? Constants.TYPE_DIVIDER : Constants.TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(NavigationViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Constants.TYPE_HEADER:
                holder.updateHeader(mPerson, mEmail);
                break;
            case Constants.TYPE_ITEM:
                final int index = position - 1;
                holder.updateData(mIcons.getResourceId(index, 0), mText[index],
                        mColors.getResourceId(index, 0), index == mCurrentPosition);
                holder.setListener(new IOnNavigationClickListener() {
                    @Override
                    public void onNavigationClick(View v, int p) {
                        notifyDataSetChanged();
                        mListener.onNavigationClick(v, index);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mText.length + 1;
    }

    public void setCurrentPosition(int currentPosition) {
        this.mCurrentPosition = currentPosition;
    }

    public void setPerson(Person person) {
        mPerson = person;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setListener(IOnNavigationClickListener listener) {
        this.mListener = listener;
    }

    public interface IOnNavigationClickListener {

        public void onNavigationClick(View v, int navigationOption);
    }

    static class NavigationViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIcon, mAvatar, mCover;
        private TextView mText, mName, mEmail;

        private int mColor, mDefaultColor;
        private IOnNavigationClickListener mListener;
        private Context mContext;

        public NavigationViewHolder(View itemView, int viewType) {
            super(itemView);
            mContext = itemView.getContext();
            switch (viewType) {
                case Constants.TYPE_HEADER:
                    mAvatar = (ImageView) itemView.findViewById(R.id.avatar);
                    mCover = (ImageView) itemView.findViewById(R.id.cover);
                    mName = (TextView) itemView.findViewById(R.id.name);
                    mEmail = (TextView) itemView.findViewById(R.id.email);
                    break;
                case Constants.TYPE_ITEM:
                    mDefaultColor = ColorUtils.getAttrColor(mContext,
                            android.R.attr.textColorPrimary);
                    mIcon = (ImageView) itemView.findViewById(R.id.icon);
                    mText = (TextView) itemView.findViewById(R.id.text);
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onNavigationClick(v, -1);
                            updateState(true);
                        }
                    });
                    break;
            }
        }

        public void updateHeader(Person person, String email) {
            mName.setText(person == null ? "" : person.getDisplayName());
            mEmail.setText(email == null ? "" : email);
            if (person != null) {
                Picasso.with(mContext)
                        .load(person.getImage().getUrl().replace("sz=50", "sz=250"))
                        .transform(new Transformation() {
                            @Override
                            public Bitmap transform(Bitmap source) {
                                Bitmap bitmap = DrawableUtils.croppedBitmap(source);
                                source.recycle();
                                return bitmap;
                            }

                            @Override
                            public String key() {
                                return "";
                            }
                        })
                        .into(mAvatar);
                Picasso.with(mContext)
                        .load(person.getCover().getCoverPhoto().getUrl())
                        .into(mCover);
            } else {
                mAvatar.setImageResource(R.drawable.ic_avatar);
                mCover.setImageResource(R.drawable.ic_cover);
            }
        }

        public void updateData(int icon, String text, int color, boolean isSelected) {
            mIcon.setImageResource(icon);
            mText.setText(text);
            mColor = color;
            updateState(isSelected);
        }

        private void updateState(boolean isSelected) {
            if (mColor == 0) return;
            itemView.setSelected(isSelected);
            mText.setTextColor(isSelected
                    ? mContext.getResources().getColor(mColor) : mDefaultColor);
        }

        public void setListener(IOnNavigationClickListener listener) {
            mListener = listener;
        }
    }
}
