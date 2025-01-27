// Generated by view binder compiler. Do not edit!
package com.example.final_project.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.final_project.R;
import com.mancj.materialsearchbar.MaterialSearchBar;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final CardView cardView;

  @NonNull
  public final ImageCarousel carousel;

  @NonNull
  public final RecyclerView categoriesList;

  @NonNull
  public final LinearLayout linearLayout;

  @NonNull
  public final RecyclerView productList;

  @NonNull
  public final MaterialSearchBar searchBar;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull CardView cardView,
      @NonNull ImageCarousel carousel, @NonNull RecyclerView categoriesList,
      @NonNull LinearLayout linearLayout, @NonNull RecyclerView productList,
      @NonNull MaterialSearchBar searchBar) {
    this.rootView = rootView;
    this.cardView = cardView;
    this.carousel = carousel;
    this.categoriesList = categoriesList;
    this.linearLayout = linearLayout;
    this.productList = productList;
    this.searchBar = searchBar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cardView;
      CardView cardView = ViewBindings.findChildViewById(rootView, id);
      if (cardView == null) {
        break missingId;
      }

      id = R.id.carousel;
      ImageCarousel carousel = ViewBindings.findChildViewById(rootView, id);
      if (carousel == null) {
        break missingId;
      }

      id = R.id.categoriesList;
      RecyclerView categoriesList = ViewBindings.findChildViewById(rootView, id);
      if (categoriesList == null) {
        break missingId;
      }

      id = R.id.linearLayout;
      LinearLayout linearLayout = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout == null) {
        break missingId;
      }

      id = R.id.productList;
      RecyclerView productList = ViewBindings.findChildViewById(rootView, id);
      if (productList == null) {
        break missingId;
      }

      id = R.id.searchBar;
      MaterialSearchBar searchBar = ViewBindings.findChildViewById(rootView, id);
      if (searchBar == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, cardView, carousel,
          categoriesList, linearLayout, productList, searchBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
