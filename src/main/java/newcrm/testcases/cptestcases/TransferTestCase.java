package newcrm.testcases.cptestcases;

import newcrm.testcases.BaseTestCase;
import newcrm.utils.testCaseDescUtils;
import org.testng.annotations.Test;

public class TransferTestCase extends AccountManagementTestCases {

    @Test(priority = 2, description = testCaseDescUtils.CPFUNDTRANS, groups = {"CP_Transfer"})
    public void testTransfer() {funcTransfer();
    }
}
