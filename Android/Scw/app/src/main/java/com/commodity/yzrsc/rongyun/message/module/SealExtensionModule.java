package com.commodity.yzrsc.rongyun.message.module;

import android.view.View;

import com.commodity.yzrsc.rongyun.SealUserInfoManager;
import com.commodity.yzrsc.rongyun.db.Friend;

import java.util.List;

import cn.rongcloud.contactcard.ContactCardPlugin;
import cn.rongcloud.contactcard.IContactCardClickCallback;
import cn.rongcloud.contactcard.IContactCardInfoProvider;
import cn.rongcloud.contactcard.message.ContactMessage;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;


public class SealExtensionModule extends DefaultExtensionModule {
    @Override
    public void onInit(String appKey) {
        super.onInit(appKey);
        ContactCardPlugin.init();
        ContactCardPlugin.getInstance().setContactCardInfoProvider(new IContactCardInfoProvider() {
            @Override
            public void getContactCardInfoProvider(final IContactCardInfoCallback contactInfoCallback) {
                SealUserInfoManager.getInstance().getFriends(new SealUserInfoManager.ResultCallback<List<Friend>>() {
                    @Override
                    public void onSuccess(List<Friend> friendList) {
                        contactInfoCallback.getContactCardInfoCallback(friendList);
                    }

                    @Override
                    public void onError(String errString) {
                        contactInfoCallback.getContactCardInfoCallback(null);
                    }
                });
            }
        });

        ContactCardPlugin.getInstance().setContactCardClickCallback(new IContactCardClickCallback() {
            @Override
            public void onContactCardMessageClick(View view, int position, ContactMessage content, UIMessage message) {
//                Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
//                Friend friend = SealUserInfoManager.getInstance().getFriendByID(content.getId());
//                if (friend == null) {
//                    UserInfo userInfo = new UserInfo(content.getId(), content.getName(), Uri.parse(TextUtils.isEmpty(content.getImgUrl()) ? RongGenerate.generateDefaultAvatar(content.getName(), content.getId()) : content.getImgUrl()));
//                    friend = CharacterParser.getInstance().generateFriendFromUserInfo(userInfo);
//                }
//                intent.putExtra("friend", friend);
//                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onAttachedToExtension(RongExtension extension) {
        super.onAttachedToExtension(extension);


    }

    @Override
    public void onDetachedFromExtension() {
        super.onDetachedFromExtension();
    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        if (conversationType.equals(Conversation.ConversationType.PRIVATE)
                || conversationType.equals(Conversation.ConversationType.GROUP)) {
           // pluginModules.add(ContactCardPlugin.getInstance());  //注释掉不显示个人名片
        }
        return pluginModules;
    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        return super.getEmoticonTabs();
    }
}
