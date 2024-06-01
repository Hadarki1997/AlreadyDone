package com.example.alreadydone;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView campaignRecyclerView;
    private RecyclerView organizationRecyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String selectedCategoryCampaigns;
    private String selectedOrganizationCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        campaignRecyclerView = findViewById(R.id.campaignRecyclerView);
        organizationRecyclerView = findViewById(R.id.organizationRecyclerView);
        Spinner spinnerCategoryCampaigns = findViewById(R.id.spinner_category_campaign);
        Spinner spinnerCategoryOrganizations = findViewById(R.id.spinner_category_organization);

        // Initialize RecyclerViews with horizontal layout
        campaignRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        organizationRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize category list
        List<String> categories = getPredefinedCategories();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategoryCampaigns.setAdapter(adapter);
        spinnerCategoryOrganizations.setAdapter(adapter);

        spinnerCategoryCampaigns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryCampaigns = parent.getItemAtPosition(position).toString();
                // Load campaigns based on selected category
                loadCampaigns();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategoryCampaigns = "הכל"; // Default category if none is selected
                // Load campaigns based on default category
                loadCampaigns();
            }
        });

        spinnerCategoryOrganizations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOrganizationCategory = parent.getItemAtPosition(position).toString();
                // Load organizations based on selected category
                loadOrganizations();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedOrganizationCategory = "הכל"; // Default category if none is selected
                // Load organizations based on default category
                loadOrganizations();
            }
        });

        // Load default campaigns and organizations
        loadCampaigns();
        loadOrganizations();
    }

    private List<String> getPredefinedCategories() {
        return Arrays.asList(getResources().getStringArray(R.array.categories_array));
    }

    private void loadCampaigns() {
        db.collection("campaigns")
                .whereEqualTo("category", selectedCategoryCampaigns)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Campaign> campaigns = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Campaign campaign = document.toObject(Campaign.class);
                            campaigns.add(campaign);
                        }
                        CampaignAdapter campaignAdapter = new CampaignAdapter(campaigns);
                        campaignRecyclerView.setAdapter(campaignAdapter);
                    } else {
                        // Handle error
                    }
                });
    }

    private void loadOrganizations() {
        db.collection("organizations")
                .whereEqualTo("category", selectedOrganizationCategory)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Organization> organizations = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Organization organization = document.toObject(Organization.class);
                            organizations.add(organization);
                        }
                        OrganizationAdapter organizationAdapter = new OrganizationAdapter(organizations);
                        organizationRecyclerView.setAdapter(organizationAdapter);
                    } else {
                        // Handle error
                    }
                });
    }
}
