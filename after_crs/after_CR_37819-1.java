/*Fix license headers.

Change-Id:I95c209535f39c1ef1384f75e19b9ab54f099a58c*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/NonInternationalizedSmsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/NonInternationalizedSmsDetector.java
//Synthetic comment -- index b9a7b79..3c1b4bf 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2012 The Android Open Source Project
*
 * Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
//Synthetic comment -- @@ -13,6 +13,7 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.tools.lint.checks;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -25,8 +26,6 @@
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
//Synthetic comment -- @@ -35,6 +34,7 @@
import lombok.ast.Expression;
import lombok.ast.MethodInvocation;
import lombok.ast.StrictListAccessor;
import lombok.ast.StringLiteral;

/** Detector looking for text messages sent to an unlocalized phone number. */
public class NonInternationalizedSmsDetector extends Detector implements Detector.JavaScanner {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/SharedPrefsDetector.java
//Synthetic comment -- index b8c86bf..41086ca 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2012 The Android Open Source Project
*
 * Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
//Synthetic comment -- @@ -13,6 +13,7 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.tools.lint.checks;

import com.android.annotations.NonNull;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ToastDetector.java
//Synthetic comment -- index d876c53..23ff794 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2012 The Android Open Source Project
*
 * Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
//Synthetic comment -- @@ -13,6 +13,7 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.tools.lint.checks;

import com.android.annotations.NonNull;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ColorUsageDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ColorUsageDetectorTest.java
//Synthetic comment -- index be60027..f30f05e 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2012 The Android Open Source Project
*
 * Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
//Synthetic comment -- @@ -13,6 +13,7 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/FragmentDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/FragmentDetectorTest.java
//Synthetic comment -- index e25bbbc..77b157e 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2012 The Android Open Source Project
*
 * Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
//Synthetic comment -- @@ -13,6 +13,7 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/JavaPerformanceDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/JavaPerformanceDetectorTest.java
//Synthetic comment -- index 100c3f7..98c664e 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2012 The Android Open Source Project
*
 * Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
//Synthetic comment -- @@ -13,6 +13,7 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/NonInternationalizedSmsDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/NonInternationalizedSmsDetectorTest.java
//Synthetic comment -- index 04cdfd3..d51acb2 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2012 The Android Open Source Project
*
 * Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
//Synthetic comment -- @@ -13,6 +13,7 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/SharedPrefsDetectorTest.java
//Synthetic comment -- index b13b344..167478d 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2012 The Android Open Source Project
*
 * Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
//Synthetic comment -- @@ -13,6 +13,7 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ToastDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/ToastDetectorTest.java
//Synthetic comment -- index edae8be..d0981e9 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2012 The Android Open Source Project
*
 * Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
//Synthetic comment -- @@ -13,6 +13,7 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/client/api/LintDriverTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/client/api/LintDriverTest.java
//Synthetic comment -- index 0fbc221..f4dba93 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2012 The Android Open Source Project
*
 * Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
//Synthetic comment -- @@ -13,6 +13,7 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.tools.lint.client.api;

import com.android.tools.lint.client.api.LintDriver.ClassEntry;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/detector/api/ClassContextTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/detector/api/ClassContextTest.java
//Synthetic comment -- index 528a53c..1f9bd2e 100644

//Synthetic comment -- @@ -1,11 +1,11 @@
/*
* Copyright (C) 2012 The Android Open Source Project
*
 * Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
 *      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
//Synthetic comment -- @@ -13,6 +13,7 @@
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.android.tools.lint.detector.api;

import junit.framework.TestCase;







