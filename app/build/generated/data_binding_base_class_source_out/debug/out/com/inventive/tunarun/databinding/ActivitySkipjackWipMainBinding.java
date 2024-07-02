// Generated by view binder compiler. Do not edit!
package com.inventive.tunarun.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.inventive.tunarun.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySkipjackWipMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView gotoPrepareRack;

  @NonNull
  public final TextView gotoQueueList;

  @NonNull
  public final TextView gotoScanBin;

  @NonNull
  public final TextView gotoTag;

  @NonNull
  public final ConstraintLayout main;

  @NonNull
  public final TextView textQueueNo;

  @NonNull
  public final TextView textUser;

  @NonNull
  public final TextView textView;

  @NonNull
  public final TextView textView22;

  @NonNull
  public final TextView textView23;

  @NonNull
  public final TextView viewDate;

  @NonNull
  public final TextView viewShift;

  private ActivitySkipjackWipMainBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView gotoPrepareRack, @NonNull TextView gotoQueueList,
      @NonNull TextView gotoScanBin, @NonNull TextView gotoTag, @NonNull ConstraintLayout main,
      @NonNull TextView textQueueNo, @NonNull TextView textUser, @NonNull TextView textView,
      @NonNull TextView textView22, @NonNull TextView textView23, @NonNull TextView viewDate,
      @NonNull TextView viewShift) {
    this.rootView = rootView;
    this.gotoPrepareRack = gotoPrepareRack;
    this.gotoQueueList = gotoQueueList;
    this.gotoScanBin = gotoScanBin;
    this.gotoTag = gotoTag;
    this.main = main;
    this.textQueueNo = textQueueNo;
    this.textUser = textUser;
    this.textView = textView;
    this.textView22 = textView22;
    this.textView23 = textView23;
    this.viewDate = viewDate;
    this.viewShift = viewShift;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySkipjackWipMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySkipjackWipMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_skipjack_wip_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySkipjackWipMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.goto_prepareRack;
      TextView gotoPrepareRack = ViewBindings.findChildViewById(rootView, id);
      if (gotoPrepareRack == null) {
        break missingId;
      }

      id = R.id.goto_queue_list;
      TextView gotoQueueList = ViewBindings.findChildViewById(rootView, id);
      if (gotoQueueList == null) {
        break missingId;
      }

      id = R.id.goto_scanBin;
      TextView gotoScanBin = ViewBindings.findChildViewById(rootView, id);
      if (gotoScanBin == null) {
        break missingId;
      }

      id = R.id.goto_tag;
      TextView gotoTag = ViewBindings.findChildViewById(rootView, id);
      if (gotoTag == null) {
        break missingId;
      }

      ConstraintLayout main = (ConstraintLayout) rootView;

      id = R.id.text_queueNo;
      TextView textQueueNo = ViewBindings.findChildViewById(rootView, id);
      if (textQueueNo == null) {
        break missingId;
      }

      id = R.id.text_user;
      TextView textUser = ViewBindings.findChildViewById(rootView, id);
      if (textUser == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      id = R.id.textView22;
      TextView textView22 = ViewBindings.findChildViewById(rootView, id);
      if (textView22 == null) {
        break missingId;
      }

      id = R.id.textView23;
      TextView textView23 = ViewBindings.findChildViewById(rootView, id);
      if (textView23 == null) {
        break missingId;
      }

      id = R.id.view_date;
      TextView viewDate = ViewBindings.findChildViewById(rootView, id);
      if (viewDate == null) {
        break missingId;
      }

      id = R.id.view_shift;
      TextView viewShift = ViewBindings.findChildViewById(rootView, id);
      if (viewShift == null) {
        break missingId;
      }

      return new ActivitySkipjackWipMainBinding((ConstraintLayout) rootView, gotoPrepareRack,
          gotoQueueList, gotoScanBin, gotoTag, main, textQueueNo, textUser, textView, textView22,
          textView23, viewDate, viewShift);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
