/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httprequestsample;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 *
 * @author hoabt2
 */
public class PersistentCookieStore extends BasicCookieStore {

    private HashMap<String, String> hashMapCookie = new HashMap<String, String>();

    /**
     *
     */
    public PersistentCookieStore() {
        super();
        load();
    }

    @Override
    public synchronized void clear() {
        super.clear();
        save();
    }

    @Override
    public synchronized boolean clearExpired(Date date) {
        boolean res = super.clearExpired(date);
        save();
        return res;
    }

    @Override
    public synchronized void addCookie(Cookie cookie) {
        super.addCookie(cookie);
        save();
    }

    @Override
    public synchronized void addCookies(Cookie[] cookie) {
        super.addCookies(cookie);
        save();
    }

    public synchronized void save() {
        System.out.println("Save Cookie");
        hashMapCookie.clear();
        List<Cookie> cookies = this.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("Name:"+cookie.getName()+",Value:"+cookie.getValue());
            hashMapCookie.put(cookie.getName(), cookie.getValue());
        }
    }

    public synchronized void load() {
        Set<String> setKey = hashMapCookie.keySet();
        System.out.println("load Cookie:"+setKey.size());
        for (String cookieName : setKey) {
            String cookieValue = hashMapCookie.get(cookieName);
            if (cookieValue != null) {
                super.addCookie(new BasicClientCookie(cookieName, cookieValue));
            }
        }

    }

}
