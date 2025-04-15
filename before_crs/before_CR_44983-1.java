/*Telephony: RADIO POWER OFF is not sent on SIM_RESET

Change-Id:Idfa8e1eef27f726ae750effdbc49aae24a621fc6*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cdma/RuimRecords.java b/src/java/com/android/internal/telephony/cdma/RuimRecords.java
//Synthetic comment -- index e8cd8f3..51953b0 100755

//Synthetic comment -- @@ -762,14 +762,18 @@
break;
case IccRefreshResponse.REFRESH_RESULT_RESET:
if (DBG) log("handleRuimRefresh with SIM_REFRESH_RESET");
                mCi.setRadioPower(false, null);
                /* Note: no need to call setRadioPower(true).  Assuming the desired
                * radio power state is still ON (as tracked by ServiceStateTracker),
                * ServiceStateTracker will call setRadioPower when it receives the
                * RADIO_STATE_CHANGED notification for the power off.  And if the
                * desired power state has changed in the interim, we don't want to
                * override it with an unconditional power on.
                */
break;
default:
// unknown refresh operation








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/SIMRecords.java b/src/java/com/android/internal/telephony/gsm/SIMRecords.java
//Synthetic comment -- index ddaf4b9..c85062b 100755

//Synthetic comment -- @@ -1156,14 +1156,18 @@
break;
case IccRefreshResponse.REFRESH_RESULT_RESET:
if (DBG) log("handleSimRefresh with SIM_REFRESH_RESET");
                mCi.setRadioPower(false, null);
                /* Note: no need to call setRadioPower(true).  Assuming the desired
                * radio power state is still ON (as tracked by ServiceStateTracker),
                * ServiceStateTracker will call setRadioPower when it receives the
                * RADIO_STATE_CHANGED notification for the power off.  And if the
                * desired power state has changed in the interim, we don't want to
                * override it with an unconditional power on.
                */
break;
default:
// unknown refresh operation







