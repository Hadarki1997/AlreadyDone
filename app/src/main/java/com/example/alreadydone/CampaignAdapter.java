package com.example.alreadydone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder> {
    private List<Campaign> campaignList;

    public CampaignAdapter(List<Campaign> campaignList) {
        this.campaignList = campaignList;
    }

    @NonNull
    @Override
    public CampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.campaign_item, parent, false);
        return new CampaignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampaignViewHolder holder, int position) {
        Campaign campaign = campaignList.get(position);
        holder.titleTextView.setText(campaign.getTitle());
        holder.raisedAmountTextView.setText("$" + campaign.getCurrentAmount());
        holder.goalAmountTextView.setText("fund raised from $" + campaign.getGoalAmount());
        holder.donatorsTextView.setText(campaign.getDonors() + " תורמים");
        holder.daysLeftTextView.setText(campaign.getDaysLeft() + " ימים שנותרו");
        holder.progressBar.setProgress((int) ((campaign.getCurrentAmount() / (float) campaign.getGoalAmount()) * 100));

        // Check if the image URL is empty and use the default image if it is
        if (campaign.getImageUrl() == null || campaign.getImageUrl().isEmpty()) {
            holder.campaignImageView.setImageResource(R.drawable.loginimg);
        } else {
            Picasso.get().load(campaign.getImageUrl()).into(holder.campaignImageView);
        }
    }

    @Override
    public int getItemCount() {
        return campaignList.size();
    }

    static class CampaignViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, raisedAmountTextView, goalAmountTextView, donatorsTextView, daysLeftTextView;
        ImageView campaignImageView;
        ProgressBar progressBar;

        public CampaignViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.campaign_title);
            raisedAmountTextView = itemView.findViewById(R.id.campaign_raised_amount);
            goalAmountTextView = itemView.findViewById(R.id.campaign_goal_amount);
            donatorsTextView = itemView.findViewById(R.id.campaign_donators);
            daysLeftTextView = itemView.findViewById(R.id.campaign_days_left);
            campaignImageView = itemView.findViewById(R.id.campaign_image);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
