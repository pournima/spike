package com.headsup.task;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

import com.headsup.models.Entry;
import com.headsup.networkconnection.XmlWebService;
import com.headsup.parser.XmlParser;
import com.headsup.safety.ConcussionAwarenessFragment;
import com.headsup.safety.ConditioningFragment;
import com.headsup.safety.EquipmentFittingFragment;
import com.headsup.safety.HeatPreparednessFragment;
import com.headsup.safety.HydrationFragment;
import com.headsup.safety.InjuryPreventionFragment;
import com.headsup.safety.NutritionFrament;

public class XmlTask extends AsyncTask<String, Void, ArrayList<Entry>> {

	InjuryPreventionFragment context;
	HydrationFragment hydrationContext;
	NutritionFrament nutritionContext;
	ConditioningFragment conditioningContext;
	HeatPreparednessFragment heatpreparednessContext;
	EquipmentFittingFragment equipmentfittingContext;
	ConcussionAwarenessFragment concusionAwarenessContext;

	public static int INJURY_PREVENTION = 0;
	public static int HYDRATION = 1;
	public static int NUTRITION = 2;
	public static int CONDITIONING = 3;
	public static int HEAT_PREPAREDNESS = 4;
	public static int EQUIPMENT = 5;
	public static int CONCUSION_AWARENESS = 6;

	public static int iActivityFlag;

	public XmlTask(InjuryPreventionFragment context) {
		this.context = context;
		iActivityFlag = INJURY_PREVENTION;
	}

	public XmlTask(HydrationFragment context) {
		this.hydrationContext = context;
		iActivityFlag = HYDRATION;
	}
	
	public XmlTask(NutritionFrament context) {
		this.nutritionContext = context;
		iActivityFlag = NUTRITION;
	}
	
	public XmlTask(ConditioningFragment context) {
		this.conditioningContext = context;
		iActivityFlag = CONDITIONING;
	}
	
	public XmlTask(HeatPreparednessFragment context) {
		this.heatpreparednessContext = context;
		iActivityFlag = HEAT_PREPAREDNESS;
	}
	
	public XmlTask(EquipmentFittingFragment context) {
		this.equipmentfittingContext = context;
		iActivityFlag = EQUIPMENT;
	}
	
	public XmlTask(ConcussionAwarenessFragment context) {
		this.concusionAwarenessContext = context;
		iActivityFlag = CONCUSION_AWARENESS;
	}

	@Override
	protected ArrayList<Entry> doInBackground(String... xml_url) {
		ArrayList<Entry> out = new ArrayList<Entry>();
		try {
			InputStream inputStream = XmlWebService.INSTANCE
					.getXmlFromUrl(xml_url[0]);
			out = XmlParser.INSTANCE.parse(inputStream);
		} catch (XmlPullParserException pullParserException) {

		} catch (IOException ioException) {

		} catch (Exception e) {

		}
		return out;
	};

	@Override
	protected void onPostExecute(ArrayList<Entry> result) {
		if (iActivityFlag == INJURY_PREVENTION) {
			context.getXmlOutput(result);
		} else if (iActivityFlag == HYDRATION) {
			hydrationContext.getXmlOutput(result);
		} else if (iActivityFlag == NUTRITION) {
			nutritionContext.getXmlOutput(result);
		}else if (iActivityFlag == CONDITIONING) {
			conditioningContext.getXmlOutput(result);
		}else if (iActivityFlag == HEAT_PREPAREDNESS) {
			heatpreparednessContext.getXmlOutput(result);
		}else if (iActivityFlag == EQUIPMENT) {
			equipmentfittingContext.getXmlOutput(result);
		}else if (iActivityFlag == CONCUSION_AWARENESS) {
			concusionAwarenessContext.getXmlOutput(result);
		}		
	}

	@Override
	protected void onPreExecute() {

	}

}
