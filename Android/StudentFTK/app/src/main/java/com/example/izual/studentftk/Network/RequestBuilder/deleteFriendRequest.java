package com.example.izual.studentftk.Network.RequestBuilder;

import com.example.izual.studentftk.Network.*;
//import com.example.izual.studentftk.Network.Request;
import com.example.izual.studentftk.Network.RequestBuilder.Request;


import java.net.URI;

public class deleteFriendRequest extends Request{
    public static final class Params {
        public static final String SocialToken = "SocialToken";
        public static final String idVk = "idVk";
        public static final String idVkFriend = "idVkFriend";
        public static final String op = "del";
    }

    public static URI BuildRequestGet(final String SocialToken,
                                      final String idVk, final String idVkFriend, final String op) {
        String[] params = {Params.SocialToken, Params.idVk, Params.idVkFriend, Params.op};
        String[] values = {SocialToken, idVk, idVkFriend, op};
        return RequestBuilder.BuildRequest(NameOfSite,
                Request.Pages.SingleFriend, Request.Methods.Manip, params, values);
    }
}
