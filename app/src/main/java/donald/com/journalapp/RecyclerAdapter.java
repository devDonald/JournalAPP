package donald.com.journalapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.ViewHolder{
    View mView;

    public RecyclerAdapter(View itemView) {
        super(itemView);
        mView=itemView;

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());

            }
        });
    }
    public void setTitle(String title){
        TextView tv_title = (TextView)mView.findViewById(R.id.tv_title);
        tv_title.setText(title);
    }
    public void setContent(String content){
        TextView tv_content=(TextView)mView.findViewById(R.id.tv_content);
        tv_content.setText(content);

    }
    public void setCategory(String category){
        TextView tv_category =(TextView)mView.findViewById(R.id.tv_category);
        tv_category.setText(category);
    }
    public void setDate(String date){
        TextView tv_date=(TextView)mView.findViewById(R.id.tv_date);
        tv_date.setText(date);
    }

    private RecyclerAdapter.ClickListener mClickListener;

    public interface ClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnClickListener(RecyclerAdapter.ClickListener clickListener){
        mClickListener = clickListener;
    }

}
