package com.peter.landing.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peter.landing.ui.screen.*
import com.peter.landing.ui.screen.affix.AffixScreen
import com.peter.landing.ui.viewModel.AffixViewModel
import com.peter.landing.ui.screen.AiScreen
import com.peter.landing.ui.screen.CartoonScreen
import com.peter.landing.ui.screen.ExamScreen
import com.peter.landing.ui.screen.HomophonyScreen
import com.peter.landing.ui.screen.StoryScreen
import com.peter.landing.ui.screen.DefinitionScreen
import com.peter.landing.ui.viewModel.DefinitionViewModel
import com.peter.landing.ui.screen.greeting.GreetingScreen
import com.peter.landing.ui.viewModel.GreetingViewModel
import com.peter.landing.ui.screen.help.HelpScreen
import com.peter.landing.ui.viewModel.HelpViewModel
import com.peter.landing.ui.screen.home.HomeScreen
import com.peter.landing.ui.viewModel.HomeViewModel
import com.peter.landing.ui.screen.ipa.IpaScreen
import com.peter.landing.ui.viewModel.IpaViewModel
import com.peter.landing.ui.screen.note.NoteScreen
import com.peter.landing.ui.viewModel.NoteViewModel
import com.peter.landing.ui.screen.plan.PlanScreen
import com.peter.landing.ui.viewModel.PlanViewModel
import com.peter.landing.ui.screen.search.SearchScreen
import com.peter.landing.ui.viewModel.SearchViewModel
import com.peter.landing.ui.screen.TermScreen
import com.peter.landing.ui.viewModel.TermViewModel
import com.peter.landing.ui.imageTranslation.ImageTranslationScreen

@Composable
fun LandingNavGraphMain(
    isDarkMode: Boolean,
    playPron: (String) -> Unit,
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    exitApp: () -> Unit,
    startDestination: String = LandingDestination.Main.Home.route
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(LandingDestination.Main.Home.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                isDarkMode = isDarkMode,
                viewModel = viewModel,
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
        composable(LandingDestination.Main.Plan.route) {
            val viewModel = hiltViewModel<PlanViewModel>()
            PlanScreen(
                isDarkMode = isDarkMode,
                viewModel = viewModel,
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.Search.route) {
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(
                isDarkMode = isDarkMode,
                viewModel = viewModel,
                navigateToDefinition = {
                    navHostController.navigate(
                        LandingDestination.Main.Search.getNavDefinitionRoute(it)
                    ) {
                        popUpTo(LandingDestination.Main.Search.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.Note.route) {
            val viewModel = hiltViewModel<NoteViewModel>()
            NoteScreen(
                isDarkMode = isDarkMode,
                viewModel = viewModel,
                playPron = playPron,
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.Ipa.route) {
            val viewModel = hiltViewModel<IpaViewModel>()
            IpaScreen(
                viewModel = viewModel,
                playPron = playPron,
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.Affix.route) {
            val viewModel = hiltViewModel<AffixViewModel>()
            AffixScreen(
                viewModel = viewModel,
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.Help.route) {
            val viewModel = hiltViewModel<HelpViewModel>()
            HelpScreen(
                viewModel = viewModel,
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.Ai.route) {
            AiScreen(
                navigateToTerms = {
                    navHostController.navigate(
                        LandingDestination.Main.Ai.getNavTermsRoute(it)
                    ) {
                        launchSingleTop = true
                    }
                },
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.Homophony.route) {
            HomophonyScreen(
                navigateToTerms = {
                    navHostController.navigate(
                        LandingDestination.Main.Homophony.getNavTermsRoute(it)
                    ) {
                        launchSingleTop = true
                    }
                },
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(LandingDestination.Main.Story.route) {
            StoryScreen(
                navigateToTerms = {
                    navHostController.navigate(
                        LandingDestination.Main.Story.getNavTermsRoute(it)
                    ) {
                        launchSingleTop = true
                    }
                },
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(LandingDestination.Main.Cartoon.route) {
            val generationState = remember { mutableStateOf(GenerationState.Idle) }
            CartoonScreen(
                navigateToTerms = {
                    navHostController.navigate(
                        LandingDestination.Main.Cartoon.getNavTermsRoute(it)
                    ) {
                        launchSingleTop = true
                    }
                },
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },onRefresh = {
                },
                generationState = generationState

            )
        }

        composable(LandingDestination.Main.Exam.route) {
            ExamScreen(
                navigateToTerms = {
                    navHostController.navigate(
                        LandingDestination.Main.Exam.getNavTermsRoute(it)
                    ) {
                        launchSingleTop = true
                    }
                },
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(LandingDestination.Main.ImageTranslation.route) {
            ImageTranslationScreen(
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        landingNavGraphStudy(
            isDarkMode = isDarkMode,
            playPron = playPron,
            navHostController = navHostController
        )
        composable(LandingDestination.General.Definition.destRoute) {
            val viewModel = hiltViewModel<DefinitionViewModel>()
            DefinitionScreen(
                viewModel = viewModel,
                playPron = playPron,
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
        composable(LandingDestination.General.Terms.destRoute) {
            val viewModel = hiltViewModel<TermViewModel>()
            TermScreen(
                viewModel = viewModel,
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
        composable(LandingDestination.General.Greeting.route) {
            val viewModel = hiltViewModel<GreetingViewModel>()
            GreetingScreen(
                viewModel = viewModel,
                exitApp = exitApp,
                navigateToHome = {
                    navHostController.navigate(LandingDestination.Main.Home.route) {
                        popUpTo(LandingDestination.General.Greeting.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
