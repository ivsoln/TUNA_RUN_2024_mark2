// Generated by view binder compiler. Do not edit!
package com.inventive.tunarun.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public final class FragmentSkipjackQueTimeBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView capActualTime;

  @NonNull
  public final TextView capArrangeFinish;

  @NonNull
  public final TextView capArrangeTime;

  @NonNull
  public final TextView capButchTime;

  @NonNull
  public final TextView capPlanTime;

  @NonNull
  public final TextView capThawTime;

  @NonNull
  public final EditText textActualTime;

  @NonNull
  public final EditText textArrangeFinish;

  @NonNull
  public final EditText textArrangeTime;

  @NonNull
  public final EditText textButchTime;

  @NonNull
  public final EditText textPlanTime;

  @NonNull
  public final EditText textThawTime;

  private FragmentSkipjackQueTimeBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView capActualTime, @NonNull TextView capArrangeFinish,
      @NonNull TextView capArrangeTime, @NonNull TextView capButchTime,
      @NonNull TextView capPlanTime, @NonNull TextView capThawTime,
      @NonNull EditText textActualTime, @NonNull EditText textArrangeFinish,
      @NonNull EditText textArrangeTime, @NonNull EditText textButchTime,
      @NonNull EditText textPlanTime, @NonNull EditText textThawTime) {
    this.rootView = rootView;
    this.capActualTime = capActualTime;
    this.capArrangeFinish = capArrangeFinish;
    this.capArrangeTime = capArrangeTime;
    this.capButchTime = capButchTime;
    this.capPlanTime = capPlanTime;
    this.capThawTime = capThawTime;
    this.textActualTime = textActualTime;
    this.textArrangeFinish = textArrangeFinish;
    this.textArrangeTime = textArrangeTime;
    this.textButchTime = textButchTime;
    this.textPlanTime = textPlanTime;
    this.textThawTime = textThawTime;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentSkipjackQueTimeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentSkipjackQueTimeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_skipjack_que_time, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentSkipjackQueTimeBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cap_actualTime;
      TextView capActualTime = ViewBindings.findChildViewById(rootView, id);
      if (capActualTime == null) {
        break missingId;
      }

      id = R.id.cap_arrangeFinish;
      TextView capArrangeFinish = ViewBindings.findChildViewById(rootView, id);
      if (capArrangeFinish == null) {
        break missingId;
      }

      id = R.id.cap_arrangeTime;
      TextView capArrangeTime = ViewBindings.findChildViewById(rootView, id);
      if (capArrangeTime == null) {
        break missingId;
      }

      id = R.id.cap_butchTime;
      TextView capButchTime = ViewBindings.findChildViewById(rootView, id);
      if (capButchTime == null) {
        break missingId;
      }

      id = R.id.cap_planTime;
      TextView capPlanTime = ViewBindings.findChildViewById(rootView, id);
      if (capPlanTime == null) {
        break missingId;
      }

      id = R.id.cap_thawTime;
      TextView capThawTime = ViewBindings.findChildViewById(rootView, id);
      if (capThawTime == null) {
        break missingId;
      }

      id = R.id.text_ActualTime;
      EditText textActualTime = ViewBindings.findChildViewById(rootView, id);
      if (textActualTime == null) {
        break missingId;
      }

      id = R.id.text_arrangeFinish;
      EditText textArrangeFinish = ViewBindings.findChildViewById(rootView, id);
      if (textArrangeFinish == null) {
        break missingId;
      }

      id = R.id.text_arrangeTime;
      EditText textArrangeTime = ViewBindings.findChildViewById(rootView, id);
      if (textArrangeTime == null) {
        break missingId;
      }

      id = R.id.text_butchTime;
      EditText textButchTime = ViewBindings.findChildViewById(rootView, id);
      if (textButchTime == null) {
        break missingId;
      }

      id = R.id.text_PlanTime;
      EditText textPlanTime = ViewBindings.findChildViewById(rootView, id);
      if (textPlanTime == null) {
        break missingId;
      }

      id = R.id.text_thawTime;
      EditText textThawTime = ViewBindings.findChildViewById(rootView, id);
      if (textThawTime == null) {
        break missingId;
      }

      return new FragmentSkipjackQueTimeBinding((ConstraintLayout) rootView, capActualTime,
          capArrangeFinish, capArrangeTime, capButchTime, capPlanTime, capThawTime, textActualTime,
          textArrangeFinish, textArrangeTime, textButchTime, textPlanTime, textThawTime);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
