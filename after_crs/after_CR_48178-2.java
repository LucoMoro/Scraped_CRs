/*Enhance performance by using ViewHolder

Change-Id:I8199b84b6822944383a79ab8ad3eb227f5e31508Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/IconListAdapter.java b/src/com/android/mms/ui/IconListAdapter.java
//Synthetic comment -- index e52a0d2..c2e4b8d 100644

//Synthetic comment -- @@ -35,7 +35,33 @@
public class IconListAdapter extends ArrayAdapter<IconListAdapter.IconListItem> {
protected LayoutInflater mInflater;
private static final int mResource = R.layout.icon_list_item;
    private ViewHolder holder;

    static class ViewHolder {
        private View mView;
        private TextView mTextView;
        private ImageView mImageView;

        public ViewHolder(View view) {
            mView = view;
        }

        public TextView getTextView() {
            if (mTextView == null) {
                mTextView = (TextView) mView.findViewById(R.id.text1);
            }

            return mTextView;
        }

        public ImageView getImageView() {
            if (mImageView == null) {
                mImageView = (ImageView) mView.findViewById(R.id.icon);
            }

            return mImageView;
        }
    }
public IconListAdapter(Context context,
List<IconListItem> items) {
super(context, mResource, items);
//Synthetic comment -- @@ -44,22 +70,22 @@

@Override
public View getView(int position, View convertView, ViewGroup parent) {
View view;
if (convertView == null) {
view = mInflater.inflate(mResource, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
} else {
view = convertView;
            holder = (ViewHolder) view.getTag();
}

// Set text field
        TextView text = holder.getTextView();
text.setText(getItem(position).getTitle());

// Set resource icon
        ImageView image = holder.getImageView();
image.setImageResource(getItem(position).getResource());

return view;








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideshowEditActivity.java b/src/com/android/mms/ui/SlideshowEditActivity.java
//Synthetic comment -- index de21e72..e0d556c 100644

//Synthetic comment -- @@ -356,6 +356,31 @@
private final int mResource;
private final LayoutInflater mInflater;
private final SlideshowModel mSlideshow;
        private ViewHolder holder;

        static class ViewHolder {
            private TextView mSlideNumnerText;
            private TextView mDurationText;
            private View mView;

            public ViewHolder(View view) {
                mView = view;
            }

            public TextView getSlide_number_text() {
                if (mSlideNumnerText == null) {
                    mSlideNumnerText = (TextView) mView.findViewById(R.id.slide_number_text);
                }
                return mSlideNumnerText;
            }

            public TextView getDuration_text() {
                if (mDurationText == null) {
                    mDurationText = (TextView) mView.findViewById(R.id.duration_text);
                }
                return mDurationText;
            }
        }

public SlideListAdapter(Context context, int resource,
SlideshowModel slideshow) {
//Synthetic comment -- @@ -374,17 +399,23 @@

private View createViewFromResource(int position, View convertView, int resource) {
SlideListItemView slideListItemView;
            if (convertView == null) {
                slideListItemView = (SlideListItemView) mInflater.inflate(
                        resource, null);
                holder = new ViewHolder(slideListItemView);
                slideListItemView.setTag(holder);
            } else {
                slideListItemView = (SlideListItemView) convertView;
                holder = (ViewHolder) convertView.getTag();
            }

// Show slide number.
            TextView text = holder.getSlide_number_text();
text.setText(mContext.getString(R.string.slide_number, position + 1));

SlideModel slide = getItem(position);
int dur = slide.getDuration() / 1000;
            text = holder.getDuration_text();
text.setText(mContext.getResources().
getQuantityString(R.plurals.slide_duration, dur, dur));








