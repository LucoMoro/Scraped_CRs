/*Enhance performance by using ViewHolder

Change-Id:I8199b84b6822944383a79ab8ad3eb227f5e31508Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/IconListAdapter.java b/src/com/android/mms/ui/IconListAdapter.java
//Synthetic comment -- index e52a0d2..c9dbdd2 100644

//Synthetic comment -- @@ -35,7 +35,32 @@
public class IconListAdapter extends ArrayAdapter<IconListAdapter.IconListItem> {
protected LayoutInflater mInflater;
private static final int mResource = R.layout.icon_list_item;

public IconListAdapter(Context context,
List<IconListItem> items) {
super(context, mResource, items);
//Synthetic comment -- @@ -44,22 +69,22 @@

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        TextView text;
        ImageView image;

View view;
if (convertView == null) {
view = mInflater.inflate(mResource, parent, false);
} else {
view = convertView;
}

// Set text field
        text = (TextView) view.findViewById(R.id.text1);
text.setText(getItem(position).getTitle());

// Set resource icon
        image = (ImageView) view.findViewById(R.id.icon);
image.setImageResource(getItem(position).getResource());

return view;








//Synthetic comment -- diff --git a/src/com/android/mms/ui/SlideshowEditActivity.java b/src/com/android/mms/ui/SlideshowEditActivity.java
//Synthetic comment -- index de21e72..649c828 100644

//Synthetic comment -- @@ -356,6 +356,28 @@
private final int mResource;
private final LayoutInflater mInflater;
private final SlideshowModel mSlideshow;

public SlideListAdapter(Context context, int resource,
SlideshowModel slideshow) {
//Synthetic comment -- @@ -374,17 +396,23 @@

private View createViewFromResource(int position, View convertView, int resource) {
SlideListItemView slideListItemView;
            slideListItemView = (SlideListItemView) mInflater.inflate(
                    resource, null);

// Show slide number.
            TextView text;
            text = (TextView) slideListItemView.findViewById(R.id.slide_number_text);
text.setText(mContext.getString(R.string.slide_number, position + 1));

SlideModel slide = getItem(position);
int dur = slide.getDuration() / 1000;
            text = (TextView) slideListItemView.findViewById(R.id.duration_text);
text.setText(mContext.getResources().
getQuantityString(R.plurals.slide_duration, dur, dur));








