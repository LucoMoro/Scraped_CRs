/*Telephony: Handle RIL_APPSTATE_ILLEGAL

Map APPSTATE_ILLEGAL to APPSTATE_READY. APPSTATE_ILLEGAL means
card is valid but the subscription is rejected by network.
All ICC operations must be permitted.
Registration will anyway not happen as network would reject
registration request.

Change-Id:Ia892c97258000aff7e2015fb9f994ff74f95c81d*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccCardApplicationStatus.java b/src/java/com/android/internal/telephony/IccCardApplicationStatus.java
//Synthetic comment -- index f3f22ea..b926f09 100644

//Synthetic comment -- @@ -135,6 +135,11 @@
case 2: newState = AppState.APPSTATE_PIN; break;
case 3: newState = AppState.APPSTATE_PUK; break;
case 4: newState = AppState.APPSTATE_SUBSCRIPTION_PERSO; break;
case 5: newState = AppState.APPSTATE_READY; break;
default:
throw new RuntimeException(








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/RIL.java b/src/java/com/android/internal/telephony/RIL.java
//Synthetic comment -- index 6131792..81361e1 100755

//Synthetic comment -- @@ -3009,7 +3009,13 @@
for (int i = 0 ; i < numApplications ; i++) {
appStatus = new IccCardApplicationStatus();
appStatus.app_type       = appStatus.AppTypeFromRILInt(p.readInt());
            appStatus.app_state      = appStatus.AppStateFromRILInt(p.readInt());
appStatus.perso_substate = appStatus.PersoSubstateFromRILInt(p.readInt());
appStatus.aid            = p.readString();
appStatus.app_label      = p.readString();







