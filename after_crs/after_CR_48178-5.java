/*Enhance performance by using ViewHolder

Change-Id:I8199b84b6822944383a79ab8ad3eb227f5e31508Signed-off-by: Roger Chen <cxr514033970@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/IconListAdapter.java b/src/com/android/mms/ui/IconListAdapter.java
//Synthetic comment -- index e52a0d2..288be7e 100644

//Synthetic comment -- @@ -35,7 +35,33 @@
public class IconListAdapter extends ArrayAdapter<IconListAdapter.IconListItem> {
protected LayoutInflater mInflater;
private static final int mResource = R.layout.icon_list_item;
    private ViewHolder mViewHolder;

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
            mViewHolder = new ViewHolder(view);
            view.setTag(mViewHolder);
} else {
view = convertView;
            mViewHolder = (ViewHolder) view.getTag();
}

// Set text field
        TextView text = mViewHolder.getTextView();
text.setText(getItem(position).getTitle());

// Set resource icon
        ImageView image = mViewHolder.getImageView();
image.setImageResource(getItem(position).getResource());

return view;







