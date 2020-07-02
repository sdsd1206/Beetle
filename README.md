# Beetle (學校專題)

以直播為特色電商平台，由以五大區塊構成。
> 登入頁面 (login)
* 買家 (buy_XXX)
1. 賣場 (store)
2. 購物車 (buycar)
3. 直播 (live)
4. 信箱 (mail_fragment)
5. 設定 (yours)
* 賣家 (sell_XXX)
1. 販售資料 (selldata)
2. 直播 (live)
3. 信箱 (mail_fragment)
4. 設定 (yours)
---

第三方直播框架 ==> 在 third.txt
1. [liveRtmpPushSDK](https://github.com/runner365/android_rtmppush_sdk) (推流)
2. [vitamio](https://github.com/yixia/VitamioBundle) (拉流框架)

伺服器：
* 透過Nginx 作為Web server，在虛擬機環境下，透過[nginx-rtmp-module](https://github.com/arut/nginx-rtmp-module)，即可進行推拉流。

---
###### 僅供觀賞，未經許可請勿下載使用。
