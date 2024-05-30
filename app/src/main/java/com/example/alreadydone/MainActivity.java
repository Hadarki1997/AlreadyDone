package com.example.alreadydone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 campaignViewPager;
    private ViewPager2 organizationViewPager;
    private Button logoutButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        campaignViewPager = findViewById(R.id.campaignViewPager);
        organizationViewPager = findViewById(R.id.organizationViewPager);
        logoutButton = findViewById(R.id.button_logout);

        // Load campaigns and organizations
        List<Campaign> campaigns = loadCampaigns();
        List<Organization> organizations = loadOrganizations();

        CampaignAdapter campaignAdapter = new CampaignAdapter(campaigns);
        OrganizationAdapter organizationAdapter = new OrganizationAdapter(organizations);

        campaignViewPager.setAdapter(campaignAdapter);
        organizationViewPager.setAdapter(organizationAdapter);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private List<Campaign> loadCampaigns() {
        List<Campaign> campaigns = new ArrayList<>();
        // Add campaigns
        campaigns.add(new Campaign("Campaign Title", "Description", "https://example.com/image.jpg", 3462, 4400, 2367, 4));
        // Add more campaigns
        return campaigns;
    }

    private List<Organization> loadOrganizations() {
        List<Organization> organizations = new ArrayList<>();
        // Add organizations
        organizations.add(new Organization("Organization Name", "Description", "https://example.com/image.jpg"));
        // Add more organizations
        return organizations;
    }
}
