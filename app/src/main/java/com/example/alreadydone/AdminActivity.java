package com.example.alreadydone;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    private MaterialButton btnAddCampaign, btnAddOrganization, btnUpdateCampaign, btnUpdateOrganization, btnAddNotification, btnRemoveCampaign, btnRemoveOrganization, btnAddCategory;
    private FirebaseFirestore db;
    private Spinner spinnerCategory;
    private String selectedCategory;
    private List<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = FirebaseFirestore.getInstance();

        // Initialize category list
        categories = new ArrayList<>();
        String[] initialCategories = getResources().getStringArray(R.array.categories_array);
        for (String category : initialCategories) {
            categories.add(category);
        }

        spinnerCategory = findViewById(R.id.spinner_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "אחר"; // קטגוריית ברירת מחדל אם לא נבחרה קטגוריה
            }
        });

        btnAddCategory = findViewById(R.id.btn_add_category);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });

        btnAddCampaign = findViewById(R.id.btn_add_campaign);
        btnAddOrganization = findViewById(R.id.btn_add_organization);
        btnUpdateCampaign = findViewById(R.id.btn_update_campaign);
        btnUpdateOrganization = findViewById(R.id.btn_update_organization);
        btnAddNotification = findViewById(R.id.btn_add_notification);
        btnRemoveCampaign = findViewById(R.id.btn_remove_campaign);
        btnRemoveOrganization = findViewById(R.id.btn_remove_organization);

        btnAddCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCampaign();
            }
        });

        btnAddOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrganization();
            }
        });

        btnUpdateCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCampaign();
            }
        });

        btnUpdateOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrganization();
            }
        });

        btnAddNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification();
            }
        });

        btnRemoveCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCampaign();
            }
        });

        btnRemoveOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOrganization();
            }
        });
    }

    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Category");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
        builder.setView(customLayout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            TextInputEditText inputCategory = customLayout.findViewById(R.id.input_category);
            String newCategory = inputCategory.getText().toString();
            if (!newCategory.isEmpty() && !categories.contains(newCategory)) {
                categories.add(newCategory);
                ((ArrayAdapter) spinnerCategory.getAdapter()).notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void addCampaign() {
        Map<String, Object> campaign = new HashMap<>();
        campaign.put("name", "New Campaign");
        campaign.put("goal", 10000);
        campaign.put("raised", 0);
        campaign.put("description", "Campaign Description");
        campaign.put("image_url", "URL to campaign image");
        campaign.put("category", selectedCategory);

        db.collection("campaigns")
                .add(campaign)
                .addOnSuccessListener(documentReference -> {
                    // Success handling
                })
                .addOnFailureListener(e -> {
                    // Failure handling
                });
    }

    private void addOrganization() {
        Map<String, Object> organization = new HashMap<>();
        organization.put("name", "New Organization");
        organization.put("type", "Health");
        organization.put("description", "Organization Description");
        organization.put("image_url", "URL to organization image");

        db.collection("organizations")
                .add(organization)
                .addOnSuccessListener(documentReference -> {
                    // Success handling
                })
                .addOnFailureListener(e -> {
                    // Failure handling
                });
    }

    private void updateCampaign() {
        // Update a specific campaign
        String campaignId = "your-campaign-id";
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Updated Campaign Name");
        updates.put("goal", 15000);
        updates.put("description", "Updated Campaign Description");
        updates.put("image_url", "Updated URL to campaign image");
        updates.put("category", selectedCategory);

        db.collection("campaigns").document(campaignId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Success handling
                })
                .addOnFailureListener(e -> {
                    // Failure handling
                });
    }

    private void updateOrganization() {
        // Update a specific organization
        String organizationId = "your-organization-id";
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Updated Organization Name");
        updates.put("type", "Updated Type");
        updates.put("description", "Updated Organization Description");
        updates.put("image_url", "Updated URL to organization image");

        db.collection("organizations").document(organizationId)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Success handling
                })
                .addOnFailureListener(e -> {
                    // Failure handling
                });
    }

    private void addNotification() {
        Map<String, Object> notification = new HashMap<>();
        notification.put("message", "New Notification");
        notification.put("date", "2024-06-01");

        db.collection("notifications")
                .add(notification)
                .addOnSuccessListener(documentReference -> {
                    // Success handling
                })
                .addOnFailureListener(e -> {
                    // Failure handling
                });
    }

    private void removeCampaign() {
        String campaignId = "your-campaign-id";
        db.collection("campaigns").document(campaignId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Success handling
                })
                .addOnFailureListener(e -> {
                    // Failure handling
                });
    }

    private void removeOrganization() {
        String organizationId = "your-organization-id";
        db.collection("organizations").document(organizationId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Success handling
                })
                .addOnFailureListener(e -> {
                    // Failure handling
                });
    }
}