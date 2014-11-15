package com.tuenti.tuentitv.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.InjectView;
import com.squareup.picasso.Picasso;
import com.tuenti.tuentitv.R;
import com.tuenti.tuentitv.ui.model.Account;
import com.tuenti.tuentitv.ui.presenter.LoginViewPresenter;
import com.tuenti.tuentitv.ui.transformation.CircleTransform;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements LoginViewPresenter.View {

  @Inject LoginViewPresenter loginPresenter;

  @InjectView(R.id.ll_accounts_container) ViewGroup ll_accounts_container;

  @Override protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.login_activity);
    super.onCreate(savedInstanceState);
    hookListeners();
    loginPresenter.setView(this);
    loginPresenter.loadAccounts();
  }

  @Override public void showAccounts(List<Account> accounts) {
    for (int i = 0; i < accounts.size(); i++) {
      View v_account = ll_accounts_container.getChildAt(i);
      showAccount(accounts.get(i), v_account);
    }
  }

  private void hookListeners() {
    for (int i = 0; i < ll_accounts_container.getChildCount(); i++) {
      View v_account = ll_accounts_container.getChildAt(i);
      configureScaleAnimator(v_account);
    }
  }

  private void configureScaleAnimator(View v_account_container) {
    View ib_account = v_account_container.findViewById(R.id.ib_account);
    View tv_account_name = v_account_container.findViewById(R.id.tv_account_name);
    ib_account.setOnFocusChangeListener(new OnFocusChangeAccountListener(tv_account_name));
  }

  private void showAccount(Account account, View v_account_container) {
    TextView tv_account_name = (TextView) v_account_container.findViewById(R.id.tv_account_name);
    ImageButton ib_account = (ImageButton) v_account_container.findViewById(R.id.ib_account);
    tv_account_name.setText(account.getName());
    Picasso.with(this)
        .load(account.getAvatarUrl())
        .transform(new CircleTransform(getResources().getDimension(R.dimen.account_item_size)))
        .placeholder(R.drawable.icn_user_default_blue)
        .into(ib_account);
  }

  @Override protected List getModules() {
    return new LinkedList();
  }

  private class OnFocusChangeAccountListener implements View.OnFocusChangeListener {

    private final View view;

    public OnFocusChangeAccountListener(View view) {
      this.view = view;
    }

    @Override public void onFocusChange(View v, boolean hasFocus) {
      float to = hasFocus ? 1.4f : 1;
      ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", to);
      scaleXAnimator.setDuration(150);
      scaleXAnimator.start();
      ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", to);
      scaleYAnimator.setDuration(150);
      scaleYAnimator.start();
    }
  }
}
