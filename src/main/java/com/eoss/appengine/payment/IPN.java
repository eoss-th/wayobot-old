package com.eoss.appengine.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Hex;

import com.eoss.appengine.bean.Orders;
import com.eoss.appengine.bean.Pusher_count;
import com.eoss.appengine.bean.UserBots;
import com.eoss.appengine.dao.AccountDAO;
import com.eoss.appengine.dao.OrdersDAO;
import com.eoss.appengine.dao.Pusher_countDAO;
import com.eoss.appengine.dao.UserBotsDAO;
import com.eoss.appengine.helper.CheckProduct;
import com.eoss.appengine.helper.SendEmail;
import com.eoss.appengine.helper.SetReqParam;
import com.eoss.appengine.service.v0_001.ExtendTime;
import com.google.appengine.api.datastore.Entity;


@SuppressWarnings("serial")
public class IPN extends HttpServlet{
	private SetReqParam srq = new SetReqParam();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String posted_data = "";
		String hex ="";
		BufferedReader in = new BufferedReader(new InputStreamReader(req.getInputStream()));
		posted_data = in.readLine();
		hex = encrypt(posted_data);
		
		Map<String, String> data = new HashMap<String, String>();
		String[] parts = posted_data.split("&");
		
		for(int i = 0; i < parts.length; i++)
		{
		    String[] valPart = parts[i].split("=");
		    data.put(valPart[0], valPart[1]);
		}
		SendEmail sendEmail = new SendEmail();
		String merchant = data.get("merchant");
		String hmac = req.getHeader("HMAC");
		String invoice = data.get("txn_id");
		String status = data.get("status");
		String custom = data.get("custom");
		String[] customParts = custom.split("_");
		String botId = customParts[0];
		double price = Double.parseDouble(data.get("amount1"));
		String email = URLDecoder.decode(data.get("email"), "UTF-8");
		Integer days = Integer.parseInt(data.get("item_number"));
		
		UserBots userbot = new UserBots();
		UserBotsDAO userBotDao = new UserBotsDAO();
		userbot.setUserBotId(botId);
	
		if(crossCheck(hmac,hex,merchant) == true) {
			CheckProduct checkProduct = new CheckProduct();
			if(checkProduct.checkProduct(data) == true) {
				OrdersDAO ordersDao = new OrdersDAO();
				Orders order = new Orders();
				order.setInvoice(invoice);
				
				Entity orderEnt = ordersDao.getOrders(invoice);								
				if(orderEnt == null) {
					order.setOrderId(Long.toString(System.nanoTime()));
					order.setStatus(status);
					order.setBotId(botId);
					order.setInvoice(invoice);
					order.setBuyTime(srq.getCurrentDate());
					order.setAccountId(customParts[1]);
					order.setPrice(price);
					ordersDao.addOrders(order);	
					
					Entity userBotent = userBotDao.getByUserBotId(userbot.getUserBotId());
					userBotent.setProperty("BuyFlag", false);
					srq.datasotorePut(userBotent);	
					
				}else {

					Integer oldstat = Integer.parseInt(orderEnt.getProperty("status").toString());
					Integer newStat = Integer.parseInt(status);
					orderEnt.setProperty("status", status);
					if(srq.datasotorePut(orderEnt) == "success") {
						if(status.equals("100") && oldstat+newStat == 101) {
							ExtendTime ex = new ExtendTime();
							if(ex.extend(botId, days)=="success") {	
								Pusher_countDAO p_countDao = new Pusher_countDAO();
								Pusher_count p_count = new Pusher_count();								
								p_count.setAccountId(customParts[1]);
								p_count.setTimeStamp(srq.getCurrentDateWithPaltform());
								AccountDAO accountDao = new AccountDAO();
								
								Entity account = accountDao.getAccount(customParts[1]);
								account.setProperty("paid", true);
								srq.datasotorePut(account);
								
								Entity PusherCount = p_countDao.getPusherCount(p_count);
								PusherCount.setProperty("maxCount", 100000);
								srq.datasotorePut(PusherCount);
							
								sendEmail.send(botId+"_"+days+"Your Payment Success!", "Payment Success", "<strong>Thank you for use our service you can con tinue use our service now</strong>", email, System.getenv("senGridEmail"));
							}
						}
					}
				}
			}else {				
				if(status.equals("100")) {
					sendEmail.send("Your Fail!", "Payment fail", "<strong>Your buy product may have some miss information please contact us</strong><br>Your invoice : "+invoice, email, System.getenv("senGridEmail"));

					Entity userBotent = userBotDao.getByUserBotId(userbot.getUserBotId());
					userBotent.setProperty("BuyFlag", true);
					srq.datasotorePut(userBotent);	
				}
			}
			
		}

	}
	
	public String encrypt(String raw_post) {
        String key = System.getenv("secret");
        String data = raw_post;
        try {
            byte[] decodedKey = key.getBytes("UTF-8");
            SecretKeySpec keySpec = new SecretKeySpec(decodedKey, "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(keySpec);
            byte[] dataBytes = data.getBytes("UTF-8");
            byte[] signatureBytes = mac.doFinal(dataBytes); 
            return Hex.encodeHexString(signatureBytes);
        }catch(Exception e) {
        	
        }
		return null;

	}

	private Boolean crossCheck(String hmac,String hex,String merchant) {
		final String merchant_id  = System.getenv("merchantId");
	
		if (hmac == null || hmac.isEmpty()) {		
			return false;	
		}
		if( merchant == null || merchant.isEmpty()) {	
			return false;		
		}
		if (!merchant.equals(merchant_id)) {			
			return false;
		}
		if(!hmac.equals(hex)) {
			return false;
		}

		return true;
	}
		
}
