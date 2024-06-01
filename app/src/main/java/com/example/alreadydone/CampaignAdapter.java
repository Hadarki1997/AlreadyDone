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
    private final List<Campaign> campaignList;

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

        // Set the campaign title
        holder.titleTextView.setText(campaign.getTitle());

        // Set the raised amount
        holder.raisedAmountTextView.setText("$" + campaign.getCurrentAmount());

        // Set the goal amount
        holder.goalAmountTextView.setText("fund raised from $" + campaign.getGoalAmount());

        // Set the number of donors
        holder.donatorsTextView.setText(campaign.getDonors() + " תורמים");

        // Set the number of days left
        holder.daysLeftTextView.setText(campaign.getDaysLeft() + " ימים שנותרו");

        // Calculate and set the progress bar value
        int progress = calculateProgress(campaign.getCurrentAmount(), campaign.getGoalAmount());
        holder.progressBar.setProgress(progress);

        // Load the campaign image or set a default image if URL is empty
        loadImage(holder.campaignImageView, campaign.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return campaignList.size();
    }

    // Helper method to calculate progress
    private int calculateProgress(double currentAmount, double goalAmount) {
        return (int) ((currentAmount / goalAmount) * 100);
    }

    // Helper method to load image
    private void loadImage(ImageView imageView, String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            imageView.setImageResource(R.drawable.loginimg);
        } else {
            Picasso.get().load(imageUrl).into(imageView);
        }
    }

    // Public static inner class for ViewHolder
    public static class CampaignViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, raisedAmountTextView, goalAmountTextView, donatorsTextView, daysLeftTextView;
        ImageView campaignImageView;
        ProgressBar progressBar;

        public CampaignViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.campaignName);
            raisedAmountTextView = itemView.findViewById(R.id.campaignRaised);
            goalAmountTextView = itemView.findViewById(R.id.campaignGoal);
            donatorsTextView = itemView.findViewById(R.id.campaignDonators);
            daysLeftTextView = itemView.findViewById(R.id.campaignDaysLeft);
            campaignImageView = itemView.findViewById(R.id.campaignImage);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
