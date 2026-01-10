package com.cdkentertainment.mobilny_usos_enhanced.views

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cdkentertainment.mobilny_usos_enhanced.R
import com.cdkentertainment.mobilny_usos_enhanced.UISingleton
import com.cdkentertainment.mobilny_usos_enhanced.getLocalized
import com.cdkentertainment.mobilny_usos_enhanced.models.SubjectTestContainer
import com.cdkentertainment.mobilny_usos_enhanced.models.Test
import com.cdkentertainment.mobilny_usos_enhanced.view_models.TestsPageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TestCardView(
    data: Test,
    modifier: Modifier = Modifier,
    onFolderClick: () -> Unit = {

    }
) {
    val context: Context = LocalContext.current
    var expanded: Boolean by rememberSaveable { mutableStateOf(false) }
    var isRootFolder: Boolean by rememberSaveable { mutableStateOf(true) }
    var lastClickedBack: Boolean by rememberSaveable { mutableStateOf(false) }
    var fetchDetailsSuccess: Boolean by rememberSaveable { mutableStateOf(true) }
    var fetchingDetails: Boolean by rememberSaveable { mutableStateOf(false) }
    var currentFolderName: String by rememberSaveable { mutableStateOf("Wszyscy uczestnicy") }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val viewModel: TestsPageViewModel = viewModel<TestsPageViewModel>()
    val currentFolderIcon: ImageVector = ImageVector.vectorResource(if (!expanded) R.drawable.rounded_groups_24 else R.drawable.rounded_search_insights_24)
    val fetchDetails: () -> Unit = {
        fetchDetailsSuccess = true
        if (viewModel.testDetails.getOrDefault(data.node_id, null) == null) {
            coroutineScope.launch {
                fetchingDetails = true
                fetchDetailsSuccess = viewModel.fetchTestDetails(data.node_id)
                fetchingDetails = false
            }
        }
        fetchingDetails = false
    }
    val retractFolder: () -> Unit = {
        lastClickedBack = true
        isRootFolder = viewModel.retractToPreviousFolder(data.node_id)
        currentFolderName = viewModel.testsSelectedFolder.getOrDefault(data.node_id, null)?.name?.getLocalized(context) ?: "N/A"
    }
    val onClick: () -> Unit = {
        fetchDetails()
        if (expanded) {
            if (isRootFolder) {
                try {
                    viewModel.changeCurrentFolder(
                        data.node_id,
                        viewModel.testDetails[data.node_id]!!
                    )
                    currentFolderName = "Wszyscy uczestnicy"
                    viewModel.clearStack(data.node_id)
                    lastClickedBack = false
                    isRootFolder = true
                    expanded = false
                } catch (e: Exception) {

                }
            } else {
                retractFolder()
            }
        } else {
            currentFolderName = data.name.getLocalized(context)
            isRootFolder = true
            expanded = true
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = UISingleton.color2,
            disabledContainerColor = UISingleton.color2,
            contentColor = UISingleton.textColor1,
            disabledContentColor = UISingleton.textColor1
        ),
        elevation = CardDefaults.cardElevation(3.dp),
        shape = RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(3.dp, RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                .clickable(onClick = onClick)
                .zIndex(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 76.dp)
                    .background(
                        UISingleton.color2,
                        RoundedCornerShape(
                            topStart = UISingleton.uiElementsCornerRadius.dp,
                            topEnd = UISingleton.uiElementsCornerRadius.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .padding(12.dp)
            ) {
                AnimatedVisibility(
                    !isRootFolder
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = UISingleton.textColor1,
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .size(36.dp)
                    )
                }
                Text(
                    text = data.course_edition?.course_name?.getLocalized(context) ?: "N/A",
                    color = UISingleton.textColor1,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        UISingleton.color3,
                        RoundedCornerShape(
                            bottomStart = UISingleton.uiElementsCornerRadius.dp,
                            bottomEnd = UISingleton.uiElementsCornerRadius.dp,
                            topStart = 0.dp,
                            topEnd = 0.dp
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 0.dp)
            ) {
                AnimatedContent(
                    targetState = currentFolderName,
                    transitionSpec = {
                        slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) + fadeIn() togetherWith slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) + fadeOut()
                    },
                    modifier = Modifier.weight(1f)
                ) { text ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium,
                        color = UISingleton.textColor4,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
                AnimatedContent(
                    targetState = currentFolderIcon,
                    transitionSpec = {
                        slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) + fadeIn() togetherWith slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) + fadeOut()
                    }
                ) { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = UISingleton.textColor4,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
        AnimatedVisibility(
            expanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
                val currentFolder: SubjectTestContainer? = viewModel.testsSelectedFolder.getOrDefault(data.node_id, null)
                AnimatedContent(
                    targetState = currentFolder,
                    transitionSpec = {
                        fadeIn() + slideIntoContainer(towards = if (!lastClickedBack) AnimatedContentTransitionScope.SlideDirection.Start else AnimatedContentTransitionScope.SlideDirection.End) togetherWith
                                fadeOut() + slideOutOfContainer(towards = if (!lastClickedBack) AnimatedContentTransitionScope.SlideDirection.Start else AnimatedContentTransitionScope.SlideDirection.End)
                    },
                    modifier = Modifier.padding(12.dp)
                ) { folder ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (folder != null) {
                            TestNameDescriptionView(
                                folder.name?.getLocalized(context) ?: "N/A",
                                folder.description?.getLocalized(context) ?: "N/A"
                            )
                            for (node in folder.subnodes_deep ?: listOf()) {
                                when {
                                    node!!.task_node_details == null && node.grade_node_details == null -> {
                                        NodeFolderView(
                                            node.name?.getLocalized(context) ?: "N/A",
                                            null
                                        ) {
                                            viewModel.changeCurrentFolder(
                                                data.node_id,
                                                node
                                            )
                                            currentFolderName = node.name?.getLocalized(context) ?: ""
                                            isRootFolder = false
                                            lastClickedBack = false
                                            onFolderClick()
                                        }
                                    }

                                    node.task_node_details != null -> {
                                        NodeTaskView(
                                            node.name?.getLocalized(context) ?: "N/A",
                                            node.task_node_details,
                                            node.task_node_details.students_points,
                                            node.subnodes_deep
                                        ) {
                                            viewModel.changeCurrentFolder(
                                                data.node_id,
                                                node
                                            )
                                            currentFolderName = node.name?.getLocalized(context) ?: ""
                                            isRootFolder = false
                                            lastClickedBack = false
                                            onFolderClick()
                                        }
                                    }

                                    node.grade_node_details != null -> {
                                        NodeGradeView(
                                            node.name?.getLocalized(context) ?: "N/A",
                                            node.grade_node_details
                                        )
                                    }
                                }
                            }
                        } else {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxWidth().padding(12.dp)
                            ) {
                                androidx.compose.animation.AnimatedVisibility(!fetchDetailsSuccess) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp))
                                            .background(UISingleton.color1)
                                            .clickable(onClick = {
                                                fetchDetails()
                                            })
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            text = "Nie udało się pobrać danych",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = UISingleton.textColor1,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Icon(
                                            imageVector = Icons.Rounded.Refresh,
                                            contentDescription = null,
                                            tint = UISingleton.textColor4,
                                            modifier = Modifier
                                                .size(48.dp)
                                                .background(UISingleton.color3, CircleShape)
                                                .padding(8.dp)
                                        )
                                    }
                                }
                                androidx.compose.animation.AnimatedVisibility(fetchDetailsSuccess){
                                    CircularProgressIndicator(
                                        color = UISingleton.textColor2
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }
}