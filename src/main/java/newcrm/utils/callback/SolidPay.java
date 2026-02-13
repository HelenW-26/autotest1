package newcrm.utils.callback;

import newcrm.utils.encryption.SolidPayEncrypt;

public class SolidPay extends CallbackBase {

	private final String path = "solid/cc/receiveWebDepositResult";
	@Override
	protected void generateRequest() {
		// TODO Auto-generated method stub
		fullPath = fullPath + "/" + regulator;
		String message ="{\"type\":\"PAYMENT\",\"payload\":{\"id\":\"8ac9a4a17d994c20017d9e9806874f19\",\"paymentType\":\"DB\",\"paymentBrand\":\"VISA\",\"amount\":\""+amount+"\",\"currency\":\""+currency+"\",\"presentationAmount\":\""+amount+"\",\"presentationCurrency\":\""+currency+"\",\"descriptor\":\"vtmarkets.com +611300945517\",\"merchantTransactionId\":\""+orderNum+"\",\"result\":{\"cvvResponse\":\"M\",\"code\":\"000.000.000\",\"description\":\"Transaction succeeded\",\"randomField1646707834\":\"Please allow for new unexpected fields to be added\"},\"resultDetails\":{\"ExtendedDescription\":\"Approved\",\"clearingInstituteName\":\"Securetrading_Omnipay\",\"ConnectorTxID1\":\"077400||true|V         M   2||1209|602|Y|P |NONE|80||false|false|false\",\"TxIdentifier\":\"481343353445198\",\"connectorId\":\"134309077400\",\"ConnectorTxID3\":\"134309077400|00|0170248134335344519800423P ||1|\",\"ConnectorTxID2\":\"525382|481343353445198|8ac9a0b57d998e32017d9e98a8544e13\",\"AcquirerResponse\":\"00\",\"reconciliationId\":\"7822.5699.7532\",\"SchemeResponseCode\":\"00\"},"+
		"\"card\":{\"bin\":\""+first6num+"\",\"last4Digits\":\""+last4num+"\",\"holder\":\"Automatioin callback test\",\"expiryMonth\":\""+month+"\",\"expiryYear\":\""+year+"\"},"+
				"\"customer\":{\"givenName\":\"Automation callback\",\"surname\":\"CRM Test\",\"mobile\":\"+309629435603\",\"email\":\"AutomationTest@test.com\",\"ip\":\"77.131.3.137\"},\"billing\":{\"street1\":\"32 Rue Roger Sonda\",\"city\":\"Ay\",\"state\":\"DEFAULT\",\"postcode\":\"51160\",\"country\":\"FR\"},\"threeDSecure\":{\"eci\":\"05\",\"verificationId\":\"AAAAAWEzQAAAADJQhTNAAAAAAAA=\",\"xid\":\"0JeOv0H6QDimfAb4ulBrjgAAAAA=\",\"paRes\":\"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\"},\"authentication\":{\"entityId\":\"8ac9a4ca778b485d01778ccb5f0a2635\"},\"customParameters\":{\"CTPE_DESCRIPTOR_TEMPLATE\":\"vtmarkets.com +611300945517\",\"CORRECTED_BRAND\":\"VISA\"},\"redirect\":{\"url\":\"https://paiement2.secure.lcl.fr/acs-pa-service/pa/paRequest\",\"parameters\":[{\"name\":\"MD\",\"value\":\"8ac9a4a17d994c20017d9e9806a04f21\"},{\"name\":\"PaReq\",\"value\":\"eJxUUtFu2zAM/BWj744oWbakgBXgLEWbIW2zrti7Y3OJt9pOZTto/76SmzSbnng84kQeic97R7T8\\nSeXoyOI99X2xo6iurq+0EdvU6DQmqJJYmtTEWyV5rHip06xKC7lVVxY3+RO9WjyS6+uutXwGM4Hs\\nDL2iK/dFO1gsytfF6sHKRGkNyE4QG3KrpQUADtJkAInWSYrsM41t0ZD9lT8857c30e36cZGvo83T\\n6v4mWq83yCYay25sB/dutciQnQGO7sXuh+EwZ+w4NIX7S0M/K7sGWWCQXTrbjCHqvdJbXVn4To9H\\nuMt+LOvmd76V48vC/dnl4V0jCxVYFQNZAYJzASYCM5d6LiSyKY9FE1qwCmAGYdBPiIfwS/7FBerf\\nFPoNOGrLd2uU9mOcEdLboWvJV3hbv2Jkl6a/3QVzy8H7xbNECZnwDDRX8QkZLlOuguNTTRCsvUFC\\ngpkUA0AWVNhpmd6b6Q589N99fAAAAP//BcCBAAAAAACQ/2sA5Uev0Q==\"},{\"name\":\"TermUrl\",\"value\":\"https://ppipe.net/connectors/asyncresponse;jsessionid=9BDF99F038A2873DAFAC07E2034ABB96.prod02-vm-con02?asyncsource=THREEDSECURE&ndcid=8ac9a4ca778b485d01778ccb5f0a2635_03c6e9a27552425fb688033fbc6a1701\"}]},\"risk\":{\"score\":\"0\"},\"timestamp\":\"2021-12-09 09:49:04+0000\",\"ndc\":\"8ac9a4ca778b485d01778ccb5f0a2635_03c6e9a27552425fb688033fbc6a1701\",\"merchantAccountId\":\"8ac9a4cd783575cd01783b74ee354e37\",\"channelName\":\"Vtmarkets.com-ecp-ST-3d\",\"source\":\"OPP\"}}";
		String iv = "253d3fb468a0e24677c28a624be0f93903";
		String secStr = null;
		try {
			secStr = SolidPayEncrypt.encrypt(key, iv, message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		body = "{\"encryptedBody\":\""+secStr+"\"}";
		this.jsonBodyHeader();
		this.headerMap.put("X-Initialization-Vector", iv);
		this.headerMap.put("X-Authentication-Tag", "");
	}

	@Override
	protected void setFullpath() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setKey() {
		// TODO Auto-generated method stub
		if("VT".equalsIgnoreCase(brand)) {
			key = "B9836EC4DED5E409901111D4F9E9FEFFBA06758D18EA989076084C01C70DF26C";
		}else if("PUG".equalsIgnoreCase(brand)) {
			key = "DD6615833B08AA4EF225E3E5E77EF34B2E87968C1117BD50E407282E5667BF58";
		}else if("VFX".equalsIgnoreCase(brand)) {
			if("ASIC".equalsIgnoreCase(regulator)){
				key = "5879812B2BCA8E2BAAFC5A825F3C5D12E61974C54118BAAB311A702E97EE09AF";
			}else{
				key = "DA88C7FC18FEDBB25B0F1870208C114A21FA847D5105562FF855B4A40ABA36E4";
			}
		}else {
			key = null;
		}
	}

}
