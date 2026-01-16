package com.cdkentertainment.bux.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.cdkentertainment.bux.R
import com.cdkentertainment.bux.UISingleton

@Composable
fun PrivacyPolicyPopupView(
    onDismiss: () -> Unit = {}
) {
    val text: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sed sem mi. Aliquam commodo purus vel suscipit fringilla. Aenean convallis orci in est lacinia, rutrum luctus augue commodo. Vestibulum vulputate, ante ultrices gravida eleifend, nisl sapien mollis turpis, nec tincidunt purus nibh vel mi. In ornare hendrerit suscipit. Interdum et malesuada fames ac ante ipsum primis in faucibus. Quisque vitae lectus eu nulla suscipit auctor facilisis rhoncus diam. Etiam eu nunc est. Duis commodo id odio sit amet placerat. Vestibulum at dignissim metus.\n" +
            "\n" +
            "Nullam justo felis, auctor vitae turpis non, imperdiet pellentesque leo. Proin porta, arcu ac sodales placerat, enim purus congue leo, et facilisis justo lacus nec enim. Curabitur vel urna ac sapien convallis iaculis non sed sem. Fusce mi libero, dapibus nec urna et, condimentum dapibus lorem. Sed posuere accumsan turpis in viverra. Pellentesque et lacus magna. Mauris eu facilisis diam. Etiam ut finibus nisi.\n" +
            "\n" +
            "Aliquam ac dui rutrum, molestie massa quis, facilisis metus. In eget diam cursus orci laoreet auctor. In ut nisi vitae augue tempus convallis sit amet sed metus. Aenean tempor lorem ac ipsum maximus, ut hendrerit magna faucibus. Curabitur aliquet erat purus, et pharetra justo maximus vitae. Aliquam quis purus dui. Suspendisse at arcu vel felis convallis tempor quis at erat. Nam pretium eros tortor. Nam ipsum felis, rutrum pharetra ultrices eu, ultrices sit amet tellus. Proin in cursus diam.\n" +
            "\n" +
            "Vestibulum id erat at leo porttitor convallis. Ut bibendum nunc id lacus blandit dignissim. Nulla convallis, arcu in luctus egestas, nisl nibh tempor augue, sed hendrerit felis eros eget mi. Maecenas pretium luctus tortor in malesuada. Vestibulum lobortis nulla at volutpat tincidunt. Quisque ut sodales dolor, eget tristique erat. In consectetur et justo in pretium. Vivamus ac porta odio. Nunc eget rhoncus mauris.\n" +
            "\n" +
            "Aliquam consequat ipsum et mollis varius. Ut id pellentesque massa. Maecenas ut tellus hendrerit, ullamcorper urna in, luctus sem. Suspendisse nec tortor quis ex facilisis aliquam nec sit amet ex. Nam at faucibus nibh. Suspendisse dapibus sapien a nibh molestie rhoncus. Curabitur molestie neque eu erat eleifend sollicitudin. Morbi convallis, quam vitae auctor maximus, metus nisi ultrices dolor, ac egestas massa mi nec neque. Integer bibendum neque et diam fringilla, eget tempor ipsum tristique. Morbi sit amet porttitor tortor. Sed fringilla consectetur fermentum. Ut eu imperdiet elit. Nulla volutpat ornare pretium. Duis mattis tortor nisl, a tincidunt ligula ornare in. Phasellus eu tortor ut ante suscipit eleifend."
    val shape: RoundedCornerShape = remember { RoundedCornerShape(UISingleton.uiElementsCornerRadius.dp) }
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    shape = shape
                )
                .background(
                    color = UISingleton.color2,
                    shape = shape
                )
                .border(
                    width = 5.dp,
                    color = UISingleton.color1,
                    shape = shape
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                PopupHeaderView(stringResource(R.string.privacy_policy))
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    color = UISingleton.textColor1,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                )
            }
            DismissPopupButtonView(onDismissRequest = onDismiss, modifier = Modifier.align(alignment = Alignment.TopEnd))
        }
    }
}