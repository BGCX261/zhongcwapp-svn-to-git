package com.ufida.gkb.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.ufida.gkb.util.Constants;
import com.ufida.gkb.util.DateTime;
import com.ufida.gkb.util.JSONUtil;
import com.ufida.gkb.util.Network;
import com.ufida.gkb.util.Result;

public class IndexService {
	public Result getSaleState() {
		Map<String, String> params = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = DateTime.getNumberOfDaysInMonth(year, month);

		cal.set(Calendar.DATE, 1);
		params.put("startDay", DateTime.format(cal.getTime()));
		cal.set(Calendar.DATE, day);
		params.put("endDay", DateTime.format(cal.getTime()));

		try {
			String response = Network.getInstance().post("/index/saleState.do",
					params);

			Map<String, String> ret = new HashMap<String, String>();

			JSONObject jsonObj = JSONUtil.toObj(response);
			ret.put("factReceiveMonth", new BigDecimal(Double.parseDouble(jsonObj.getString("factReceiveMonth"))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			ret.put("contractNumMonth", jsonObj.getString("contractNumMonth"));
			ret.put("createActivityNumMonth",
					jsonObj.getString("createActivityNumMonth"));
			ret.put("contractAmountMonth",
					new BigDecimal(Double.parseDouble(jsonObj.getString("contractAmountMonth"))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
			ret.put("createAccountNumMonth",
					jsonObj.getString("createAccountNumMonth"));
			Result result = Result.success(Constants.MSG_SUCCESS);
			result.setObj(ret);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return Result.fail(Constants.MSG_NETWORK_ERROR);
		}
	}
}
