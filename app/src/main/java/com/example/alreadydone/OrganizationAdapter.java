package com.example.alreadydone;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class OrganizationAdapter extends RecyclerView.Adapter<OrganizationAdapter.OrganizationViewHolder> {
    private List<Organization> organizationList;

    public OrganizationAdapter(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }

    @NonNull
    @Override
    public OrganizationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.organization_item, parent, false);
        return new OrganizationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizationViewHolder holder, int position) {
        Organization organization = organizationList.get(position);
        holder.nameTextView.setText(organization.getName());
        holder.descriptionTextView.setText(organization.getDescription());
        Picasso.get().load(organization.getImageUrl()).into(holder.organizationImageView);
    }

    @Override
    public int getItemCount() {
        return organizationList.size();
    }

    static class OrganizationViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView;
        ImageView organizationImageView;

        public OrganizationViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameOrganization);
            descriptionTextView = itemView.findViewById(R.id.descriptionOrganization);
            organizationImageView = itemView.findViewById(R.id.organizationImageView);
        }
    }
}
