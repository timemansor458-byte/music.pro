package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tablet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SyncDevice
import com.example.model.UserProfile
import com.example.ui.theme.VibrantAccentAmber
import com.example.ui.theme.VibrantAccentCyan
import com.example.ui.theme.VibrantDarkActiveSurface
import com.example.ui.theme.VibrantDarkBackground
import com.example.ui.theme.VibrantDarkSurface
import com.example.ui.theme.VibrantDarkSurfaceVariant
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantPurpleContainer
import com.example.ui.theme.VibrantPurpleDark
import com.example.ui.theme.VibrantPurpleLight
import com.example.ui.theme.VibrantTextMuted
import com.example.ui.theme.VibrantTextPrimary
import com.example.ui.theme.VibrantTextSecondary

@Composable
fun SyncProfileDialog(
  userProfile: UserProfile,
  onDismiss: () -> Unit,
  onTriggerSync: () -> Unit,
  onToggleAutoSync: () -> Unit,
  onUpdateProfile: (name: String, email: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var isEditingProfile by remember { mutableStateOf(false) }
  var editName by remember { mutableStateOf(userProfile.name) }
  var editEmail by remember { mutableStateOf(userProfile.email) }

  val infiniteTransition = rememberInfiniteTransition(label = "sync_rotation")
  val rotationAngle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1000, easing = LinearEasing)
    ),
    label = "sync_rotate"
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = modifier.testTag("sync_profile_dialog"),
    shape = RoundedCornerShape(24.dp),
    containerColor = VibrantDarkSurface,
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.CloudSync,
            contentDescription = null,
            tint = VibrantPurple,
            modifier = Modifier.size(28.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "المزامنة والحساب السحابي",
            color = VibrantTextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
          )
        }
      }
    },
    text = {
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // User Profile Header Card
        item {
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = VibrantDarkActiveSurface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              if (isEditingProfile) {
                Text(
                  text = "تعديل بيانات الحساب",
                  color = VibrantPurple,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                  value = editName,
                  onValueChange = { editName = it },
                  label = { Text("الاسم") },
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantPurple,
                    unfocusedBorderColor = VibrantDarkSurfaceVariant,
                    focusedTextColor = VibrantTextPrimary,
                    unfocusedTextColor = VibrantTextPrimary
                  ),
                  modifier = Modifier.fillMaxWidth().testTag("edit_profile_name")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                  value = editEmail,
                  onValueChange = { editEmail = it },
                  label = { Text("البريد الإلكتروني") },
                  colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VibrantPurple,
                    unfocusedBorderColor = VibrantDarkSurfaceVariant,
                    focusedTextColor = VibrantTextPrimary,
                    unfocusedTextColor = VibrantTextPrimary
                  ),
                  modifier = Modifier.fillMaxWidth().testTag("edit_profile_email")
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.End
                ) {
                  TextButton(onClick = { isEditingProfile = false }) {
                    Text("إلغاء", color = VibrantTextMuted)
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                  Button(
                    onClick = {
                      onUpdateProfile(editName, editEmail)
                      isEditingProfile = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple)
                  ) {
                    Text("حفظ", color = VibrantDarkBackground, fontWeight = FontWeight.Bold)
                  }
                }
              } else {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(52.dp)
                      .clip(CircleShape)
                      .background(VibrantPurpleContainer),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = userProfile.avatarEmoji,
                      fontSize = 26.sp
                    )
                  }
                  Spacer(modifier = Modifier.width(14.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text(
                      text = userProfile.name,
                      color = VibrantTextPrimary,
                      fontWeight = FontWeight.Bold,
                      fontSize = 16.sp
                    )
                    Text(
                      text = userProfile.email,
                      color = VibrantTextMuted,
                      fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(VibrantPurple.copy(alpha = 0.18f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                      Text(
                        text = userProfile.planName,
                        color = VibrantPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                      )
                    }
                  }
                  IconButton(onClick = { isEditingProfile = true }) {
                    Icon(
                      imageVector = Icons.Default.Edit,
                      contentDescription = "تعديل الحساب",
                      tint = VibrantPurpleLight,
                      modifier = Modifier.size(20.dp)
                    )
                  }
                }
              }
            }
          }
        }

        // Sync Status & Action
        item {
          Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = VibrantDarkActiveSurface),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = if (userProfile.isSyncing) Icons.Default.Sync else Icons.Default.CloudDone,
                    contentDescription = null,
                    tint = if (userProfile.isSyncing) VibrantAccentAmber else VibrantAccentCyan,
                    modifier = Modifier
                      .size(22.dp)
                      .then(if (userProfile.isSyncing) Modifier.rotate(rotationAngle) else Modifier)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(
                      text = if (userProfile.isSyncing) "جارِ مزامنة السحابة..." else "المزامنة نشطة ومحدثة",
                      color = VibrantTextPrimary,
                      fontWeight = FontWeight.SemiBold,
                      fontSize = 13.sp
                    )
                    Text(
                      text = "آخر تحديث: ${userProfile.lastSyncTime}",
                      color = VibrantTextMuted,
                      fontSize = 11.sp
                    )
                  }
                }

                Button(
                  onClick = onTriggerSync,
                  enabled = !userProfile.isSyncing,
                  colors = ButtonDefaults.buttonColors(
                    containerColor = VibrantPurpleDark,
                    contentColor = VibrantTextPrimary
                  ),
                  shape = RoundedCornerShape(12.dp),
                  modifier = Modifier.testTag("sync_now_button")
                ) {
                  if (userProfile.isSyncing) {
                    CircularProgressIndicator(
                      color = VibrantPurple,
                      modifier = Modifier.size(16.dp),
                      strokeWidth = 2.dp
                    )
                  } else {
                    Text("مزامنة الآن", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                  }
                }
              }

              Spacer(modifier = Modifier.height(12.dp))

              // Auto Sync Switch Row
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = "المزامنة الفورية التلقائية",
                    color = VibrantTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                  )
                  Text(
                    text = "مزامنة القوائم والمفضلة فورياً عند أي تغيير",
                    color = VibrantTextMuted,
                    fontSize = 11.sp
                  )
                }
                Switch(
                  checked = userProfile.autoSyncEnabled,
                  onCheckedChange = { onToggleAutoSync() },
                  colors = SwitchDefaults.colors(
                    checkedThumbColor = VibrantPurple,
                    checkedTrackColor = VibrantPurpleContainer,
                    uncheckedThumbColor = VibrantTextMuted,
                    uncheckedTrackColor = VibrantDarkSurfaceVariant
                  ),
                  modifier = Modifier.testTag("auto_sync_switch")
                )
              }
            }
          }
        }

        // Connected Devices Section
        item {
          Text(
            text = "الأجهزة المتصلة بحسابك (3 أجهزة)",
            color = VibrantTextSecondary,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }

        items(userProfile.connectedDevices) { device ->
          DeviceItemCard(device = device)
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.testTag("close_sync_dialog_button")
      ) {
        Text("إغلاق", color = VibrantDarkBackground, fontWeight = FontWeight.Bold)
      }
    }
  )
}

@Composable
private fun DeviceItemCard(device: SyncDevice) {
  val icon = when (device.type) {
    "tablet" -> Icons.Default.Tablet
    "desktop" -> Icons.Default.Laptop
    else -> Icons.Default.PhoneAndroid
  }

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (device.isCurrent) VibrantDarkActiveSurface else VibrantDarkSurfaceVariant.copy(alpha = 0.5f)
    ),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(if (device.isCurrent) VibrantPurpleContainer else VibrantDarkSurfaceVariant),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = if (device.isCurrent) VibrantPurple else VibrantTextMuted,
          modifier = Modifier.size(20.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = device.name,
            color = VibrantTextPrimary,
            fontWeight = if (device.isCurrent) FontWeight.Bold else FontWeight.Normal,
            fontSize = 13.sp
          )
          if (device.isCurrent) {
            Spacer(modifier = Modifier.width(6.dp))
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(VibrantPurple.copy(alpha = 0.2f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "هذا الجهاز",
                color = VibrantPurple,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
        Text(
          text = "الحالة: ${device.lastActiveTime}",
          color = if (device.isCurrent) VibrantAccentCyan else VibrantTextMuted,
          fontSize = 11.sp
        )
      }
      Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = null,
        tint = if (device.isCurrent) VibrantAccentCyan else VibrantTextMuted.copy(alpha = 0.5f),
        modifier = Modifier.size(18.dp)
      )
    }
  }
}
