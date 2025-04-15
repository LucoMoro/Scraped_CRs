/*Enhance performance by using ViewHolder

Change-Id:I8199b84b6822944383a79ab8ad3eb227f5e31508Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/IconListAdapter.java b/src/com/android/mms/ui/IconListAdapter.java
//Synthetic comment -- index e52a0d2..c9dbdd2 100644

//Synthetic comment -- @@ -35,7 +35,32 @@
public class IconListAdapter extends ArrayAdapter<IconListAdapter.IconListItem> {
protected LayoutInflater mInflater;
private static final int mResource = R.layout.icon_list_item;
    private ViewHolder holder;

    static class ViewHolder {
        private View v;
        private TextView tv;
        private ImageView image;
        public ViewHolder(View view) {
            v = view;
        }

        public TextView getTextView() {
            if (tv == null) {
                tv = (TextView) v.findViewById(R.id.text1);
            }

            return tv;
        }

        public ImageView getImageView() {
            if (image == null) {
                image = (ImageView) v.findViewById(R.id.icon);
            }

            return image;
        }
    }
public IconListAdapter(Context context,
List<IconListItem> items) {
super(context, mResource, items);
//Synthetic comment -- @@ -44,22 +69,22 @@

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
//Synthetic comment -- index de21e72..649c828 100644

//Synthetic comment -- @@ -356,6 +356,28 @@
private final int mResource;
private final LayoutInflater mInflater;
private final SlideshowModel mSlideshow;
        private ViewHolder holder;
        static class ViewHolder {
            private TextView slide_number_text;
            private TextView duration_text;
            private View v;

            public ViewHolder(View view) {
                v = view;
            }
            public TextView getSlide_number_text() {
                if (slide_number_text == null) {
                    slide_number_text = (TextView) v.findViewById(R.id.slide_number_text);
                }
                return slide_number_text;
            }
            public TextView getDuration_text() {
                if (duration_text == null) {
                    duration_text = (TextView) v.findViewById(R.id.duration_text);
                }
                return duration_text;
            }
        }

public SlideListAdapter(Context context, int resource,
SlideshowModel slideshow) {
//Synthetic comment -- @@ -374,17 +396,23 @@

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








