package com.jeoktoma.drivemate

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun AppNavigation(navController: NavHostController) {
    val selectedItem = remember { mutableStateOf(0) }

    NavHost(navController = navController, startDestination = "loginScreen") {
        composable("loginScreen") { LoginScreen(navController) }
        composable("signUpScreen") { SignUpScreen(navController) }
        composable("mainScreen") {
            selectedItem.value = 0 // MainScreen을 선택 상태로 설정
            MainScreen(navController, "코너링이 훌룡하시네요,", "Gildong Hong", selectedItem)
        }
        composable("pathScreen") {
            selectedItem.value = 2 // PathScreen을 선택 상태로 설정
            PathScreen(navController, selectedItem)
        }
        composable("reportScreen") {
            selectedItem.value = 1 // ReportScreen을 선택 상태로 설정
            ReportScreen(navController, selectedItem)
        }
        composable("tipScreen") {
            selectedItem.value = 3 // TipScreen을 선택 상태로 설정
            TipScreen(navController, selectedItem)
        }
        composable("profileScreen") {
            selectedItem.value = 4 // ProfileScreen을 선택 상태로 설정
            ProfileScreen(navController, selectedItem)
        }
        composable("editProfileScreen") {
            selectedItem.value = 4
            EditProfileScreen(navController, selectedItem)
        }

        composable(
            route = "tipDetailsScreen/{tipTitle}/{tipDetailsJson}",
            arguments = listOf(
                navArgument("tipTitle") { type = NavType.StringType },
                navArgument("tipDetailsJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val tipTitle = backStackEntry.arguments?.getString("tipTitle") ?: "Unknown"
            val tipDetailsJson = backStackEntry.arguments?.getString("tipDetailsJson") ?: "[]"
            val tipDetails = Gson().fromJson<List<TipDetail>>(Uri.decode(tipDetailsJson), object : TypeToken<List<TipDetail>>() {}.type)
            selectedItem.value = 3
            TipDetailsScreen(navController = navController, tipTitle = tipTitle, tips = tipDetails, selectedItem)
        }

        composable(
            route = "segmentSurveyScreen/{segmentIndex}/{totalSegments}/{estimatedTime}/{actualTime}",
            arguments = listOf(
                navArgument("segmentIndex") { type = NavType.IntType },
                navArgument("totalSegments") { type = NavType.IntType },
                navArgument("estimatedTime") { type = NavType.StringType },
                navArgument("actualTime") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val segmentIndex = backStackEntry.arguments?.getInt("segmentIndex") ?: 0
            val totalSegments = backStackEntry.arguments?.getInt("totalSegments") ?: 1
            val estimatedTime = backStackEntry.arguments?.getString("estimatedTime") ?: "0 min"
            val actualTime = backStackEntry.arguments?.getString("actualTime") ?: "0 min"

            // SegmentSurveyScreen 호출
            SegmentSurveyScreen(
                segmentIndex = segmentIndex,
                totalSegments = totalSegments,
                segmentImage = null, // 이미지 리소스를 전달할 경우 여기에 추가
                estimatedTime = estimatedTime,
                actualTime = actualTime,
                onNext = {
                    if (segmentIndex < totalSegments - 1) {
                        // 다음 구간으로 이동
                        navController.navigate(
                            "segmentSurveyScreen/${segmentIndex + 1}/$totalSegments/$estimatedTime/$actualTime"
                        )
                    } else {
                        // 전체 설문 화면으로 이동
                        navController.navigate("overallSurveyScreen")
                    }
                },
                navController = navController
            )
        }

        composable("overallSurveyScreen") {
            OverallSurveyScreen(
                onComplete = {
                    // 필요한 로직 추가 (예: 데이터 저장)
                },
                navController = navController
            )
        }



    }
}