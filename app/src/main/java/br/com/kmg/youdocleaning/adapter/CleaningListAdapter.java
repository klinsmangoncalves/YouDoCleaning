package br.com.kmg.youdocleaning.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import br.com.kmg.youdocleaning.R;
import br.com.kmg.youdocleaning.listener.OnReadCleaningListListener;
import br.com.kmg.youdocleaning.model.Cleaning;
import br.com.kmg.youdocleaning.util.DateUtil;

public class CleaningListAdapter extends FirebaseRecyclerAdapter<Cleaning, CleaningListAdapter.CleaningViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CleaningListAdapter(@NonNull FirebaseRecyclerOptions<Cleaning> options) {
        super(options);
    }

    private OnReadCleaningListListener mReadListListener;

    public void setmReadListListener(OnReadCleaningListListener mReadListListener) {
        this.mReadListListener = mReadListListener;
    }

    @Override
    public CleaningViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new CleaningViewHolder(inflater.inflate(R.layout.cleaning_item, viewGroup, false));
    }

    @Override
    protected void onBindViewHolder(final CleaningViewHolder viewHolder, int position, Cleaning cleaning) {
        if(mReadListListener != null){
            mReadListListener.onReadCleaningList();
            mReadListListener = null;
        }
        if (cleaning.getIdDepartment() != null) {
            viewHolder.tvDepartmentName.setText(cleaning.getIdDepartment());
            String duration = DateUtil.getDateStringTimePeriodFromDate(cleaning.getStartCleaning(), cleaning.getFinishCleaning());
            viewHolder.tvCleaningDuration.setText(duration);
//            Glide.with(viewHolder.ivCompanyLogo.getContext())
//                    .load(cleaning.getImageUrl()).into(viewHolder.ivCompanyLogo);
        }

    }

    public static class CleaningViewHolder extends RecyclerView.ViewHolder {
        TextView tvDepartmentName;
        ImageView ivCompanyLogo;
        TextView tvCleaningDuration;

        public CleaningViewHolder(View v) {
            super(v);
            tvDepartmentName = itemView.findViewById(R.id.tv_department_name);
            ivCompanyLogo = itemView.findViewById(R.id.iv_company_logo);
            tvCleaningDuration = itemView.findViewById(R.id.tv_cleaning_duration);
        }
    }

}
