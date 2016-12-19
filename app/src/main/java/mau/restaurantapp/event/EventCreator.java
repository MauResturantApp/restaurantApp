package mau.restaurantapp.event;

import org.greenrobot.eventbus.EventBus;

import mau.restaurantapp.event.events.AskForDeleteTabeEvent;
import mau.restaurantapp.event.events.FragContainerChangedEvent;
import mau.restaurantapp.event.events.GuestUserCheckoutEvent;
import mau.restaurantapp.event.events.HideCartEvent;
import mau.restaurantapp.event.events.IsAdminEvent;
import mau.restaurantapp.event.events.LogUserInEvent;
import mau.restaurantapp.event.events.NewItemToCartEvent;
import mau.restaurantapp.event.events.NewUserFailedEvent;
import mau.restaurantapp.event.events.NewUserSuccesfullEvent;
import mau.restaurantapp.event.events.OnFailedLogIn;
import mau.restaurantapp.event.events.OnSuccesfullLogInEvent;
import mau.restaurantapp.event.events.OrderSuccessfulEvent;
import mau.restaurantapp.event.events.ResumeTabsEvent;
import mau.restaurantapp.event.events.ShopClosedEvent;
import mau.restaurantapp.event.events.ShowAskforLoginDialogEvent;
import mau.restaurantapp.event.events.ShowCartEvent;
import mau.restaurantapp.event.events.ShowLogInDialogEvent;
import mau.restaurantapp.event.events.ShowSignupDialogEvent;
import mau.restaurantapp.event.events.SignOutEvent;
import mau.restaurantapp.event.events.SignUpBtnClicked;
import mau.restaurantapp.event.events.TabsChangedEvent;

/**
 * Created by AnwarC on 15/11/2016.
 */

public class EventCreator {
    private EventBus bus = EventBus.getDefault();

    public void fragContainerChanged() {
        FragContainerChangedEvent event = new FragContainerChangedEvent();
        bus.post(event);
    }

    public void logUserIn() {
        LogUserInEvent event = new LogUserInEvent();
        bus.post(event);
    }

    public void newItemToCart() {
        NewItemToCartEvent event = new NewItemToCartEvent();
        bus.post(event);
    }

    public void newUserFailed() {
        NewUserFailedEvent event = new NewUserFailedEvent();
        bus.post(event);
    }

    public void newUserSuccesfull() {
        NewUserSuccesfullEvent event = new NewUserSuccesfullEvent();
        bus.post(event);
    }

    public void failedLogin() {
        OnFailedLogIn event = new OnFailedLogIn();
        bus.post(event);
    }

    public void succesfullLogin() {
        OnSuccesfullLogInEvent event = new OnSuccesfullLogInEvent();
        bus.post(event);
    }

    public void signOut() {
        SignOutEvent event = new SignOutEvent();
        bus.post(event);
    }

    public void shopCLosed() {
        ShopClosedEvent event = new ShopClosedEvent();
        bus.post(event);
    }

    public void resumeTabs() {
        ResumeTabsEvent event = new ResumeTabsEvent();
        bus.post(event);
    }

    public void signUpNewUser() {
        SignUpBtnClicked event = new SignUpBtnClicked();
        bus.post(event);
    }

    public void guestCheckout() {
        GuestUserCheckoutEvent event = new GuestUserCheckoutEvent();
        bus.post(event);
    }

    public void showLoginDialog() {
        ShowLogInDialogEvent event = new ShowLogInDialogEvent();
        bus.post(event);
    }

    public void showSignupDialog() {
        ShowSignupDialogEvent event = new ShowSignupDialogEvent();
        bus.post(event);
    }

    public void showAskForLoginDialog() {
        ShowAskforLoginDialogEvent event = new ShowAskforLoginDialogEvent();
        bus.post(event);
    }

    public void isAdmin() {
        IsAdminEvent event = new IsAdminEvent();
        bus.post(event);
    }

    public void tabsChanged() {
        TabsChangedEvent event = new TabsChangedEvent();
        bus.post(event);
    }

    public void orderSuccessful() {
        OrderSuccessfulEvent event = new OrderSuccessfulEvent();
        bus.post(event);
    }

    public void showCart() {
        ShowCartEvent event = new ShowCartEvent();
        bus.post(event);
    }

    public void hideCart() {
        HideCartEvent event = new HideCartEvent();
        bus.post(event);
    }

    public void confirmDeleteTab(String id) {
        AskForDeleteTabeEvent event = new AskForDeleteTabeEvent(id);
        bus.post(event);
    }
}
