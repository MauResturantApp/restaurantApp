package mau.resturantapp.event;

import org.greenrobot.eventbus.EventBus;

import mau.resturantapp.event.events.FragContainerChangedEvent;
import mau.resturantapp.event.events.GuestUserCheckoutEvent;
import mau.resturantapp.event.events.HideCartEvent;
import mau.resturantapp.event.events.IsAdminEvent;
import mau.resturantapp.event.events.LogUserInEvent;
import mau.resturantapp.event.events.NewItemToCartEvent;
import mau.resturantapp.event.events.NewUserFailedEvent;
import mau.resturantapp.event.events.NewUserSuccesfullEvent;
import mau.resturantapp.event.events.OnFailedLogIn;
import mau.resturantapp.event.events.OnSuccesfullLogInEvent;
import mau.resturantapp.event.events.OrderSuccessfulEvent;
import mau.resturantapp.event.events.ResumeTabsEvent;
import mau.resturantapp.event.events.ShopClosedEvent;
import mau.resturantapp.event.events.ShowAskforLoginDialogEvent;
import mau.resturantapp.event.events.ShowCartEvent;
import mau.resturantapp.event.events.ShowLogInDialogEvent;
import mau.resturantapp.event.events.ShowSignupDialogEvent;
import mau.resturantapp.event.events.SignOutEvent;
import mau.resturantapp.event.events.SignUpBtnClicked;
import mau.resturantapp.event.events.TabsChangedEvent;

/**
 * Created by AnwarC on 15/11/2016.
 */

public class EventCreator {
    private EventBus bus = EventBus.getDefault();

    public void fragContainerChanged(){
        FragContainerChangedEvent event = new FragContainerChangedEvent();
        bus.post(event);
    }

    public void logUserIn(){
        LogUserInEvent event = new LogUserInEvent();
        bus.post(event);
    }
    public void newItemToCart(){
        NewItemToCartEvent event = new NewItemToCartEvent();
        bus.post(event);
    }
    public void newUserFailed(){
        NewUserFailedEvent event = new NewUserFailedEvent();
        bus.post(event);
    }
    public void newUserSuccesfull(){
        NewUserSuccesfullEvent event = new NewUserSuccesfullEvent();
        bus.post(event);
    }
    public void failedLogin(){
        OnFailedLogIn event = new OnFailedLogIn();
        bus.post(event);
    }
    public void succesfullLogin(){
        OnSuccesfullLogInEvent event = new OnSuccesfullLogInEvent();
        bus.post(event);
    }
    public void signOut(){
        SignOutEvent event = new SignOutEvent();
        bus.post(event);
    }

    public void shopCLosed(){
        ShopClosedEvent event = new ShopClosedEvent();
        bus.post(event);
    }

    public void resumeTabs(){
        ResumeTabsEvent event = new ResumeTabsEvent();
        bus.post(event);
    }
    public void signUpNewUser(){
        SignUpBtnClicked event = new SignUpBtnClicked();
        bus.post(event);
    }

    public void guestCheckout(){
        GuestUserCheckoutEvent event = new GuestUserCheckoutEvent();
        bus.post(event);
    }
    public void showLoginDialog(){
        ShowLogInDialogEvent event = new ShowLogInDialogEvent();
        bus.post(event);
    }
    public void showSignupDialog(){
        ShowSignupDialogEvent event = new ShowSignupDialogEvent();
        bus.post(event);
    }
    public void showAskForLoginDialog(){
        ShowAskforLoginDialogEvent event = new ShowAskforLoginDialogEvent();
        bus.post(event);
    }

    public void isAdmin(){
        IsAdminEvent event = new IsAdminEvent();
        bus.post(event);
    }

    public void tabsChanged(){
        TabsChangedEvent event = new TabsChangedEvent();
        bus.post(event);
    }

    public void orderSuccessful(){
        OrderSuccessfulEvent event = new OrderSuccessfulEvent();
        bus.post(event);
    }

    public void showCart(){
        ShowCartEvent event = new ShowCartEvent();
        bus.post(event);
    }

    public void hideCart(){
        HideCartEvent event = new HideCartEvent();
        bus.post(event);
    }
}
