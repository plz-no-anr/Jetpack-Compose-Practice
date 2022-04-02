package com.psg.jetpackcomposepractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.psg.jetpackcomposepractice.ui.theme.JetpackComposePracticeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { // Compose가 시작되는 부분
            var isFavorite by rememberSaveable { // rememberSaveable 생명주기가 다시 돌아도 데이터를 보존
                mutableStateOf(false)
            }
            JetpackComposePracticeTheme {
                // A surface container using the 'background' color from the theme
//                    Test1()
//                    Test3()
                Test4(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(16.dp),
                    isFavorite
                ) { favorite -> // 콜백함수
                    isFavorite = favorite
                }
            }
        }
    }
}

/**
 * Column, Row, Text
 */
//@Preview(showBackground = true)
@Composable
fun Test1() {
    // LinearLayout과 비슷 Vertical
    Column(
        modifier = Modifier
            .fillMaxSize() // Match_Parent랑 비슷한 속성
            .background(color = Color.Blue)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // 정렬
        verticalArrangement = Arrangement.Bottom
    ) {
        Text("Hello")
        Spacer(modifier = Modifier.width(16.dp)) // 속성 지정
        Text("World")
    }
    // LinearLayout과 비슷 Horizontal
    Row { //
        for (i in 1..50) {
            Text("텍스트$i")
        }
    }
}

/**
 * Box
 */
//@Preview(showBackground = true)
@Composable
fun Test2() {
    // Box: FrameLayout과 비슷
    Box(
        modifier = Modifier
            .background(color = Color.Green)
            .fillMaxWidth()
            .height(200.dp),
    ) {
        Text("Hello")
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text("sadassdafasdf!@")
        }

    }
}

/**
 * 리스트, LazyColumn
 */
//@Preview(showBackground = true)
@Composable
fun Test3() {
    val scrollState = rememberScrollState() // 스크롤 상태를 기억

    LazyColumn(
        modifier = Modifier
            .background(color = Color.Green)
            .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp), // 아이템간의 간격
    ) {
        item {
            Text("Header")
        }
        items(50) { index ->
            Text("텍스트 $index")
        }
        item {
            Text("Footer")
        }
    }
}

/**
 * Image, Card, 상태
 */
//@Preview(showBackground = true)
@Composable
fun Test4(
    modifier: Modifier = Modifier, // 외부에서 받는게 재사용하기 편리함
    isFavorite: Boolean,
    onTabFavorite: (Boolean) -> Unit, // 콜백함수
) {
//    var isFavorite by remember { // 상태를 기억, by는 value를 생략
//        mutableStateOf(false)
//    }
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
    ) {
        Box(
            modifier = Modifier.height(200.dp),
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.down),
                contentDescription = "Avengers", // 이미지 설명
                contentScale = ContentScale.Crop, // 기존 이미지뷰의 Scale속성
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd,
            ) {
                IconButton(onClick = {
                    onTabFavorite(!isFavorite)
//                    isFavorite = !isFavorite
                }) {
                    // 상태가 바뀌면 UI를 다시 그림
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                        contentDescription = "favorite",
                        tint = Color.Red
                    )

                }
            }
        }
    }
}

/**
 * Scaffold, TextField, Button, 구조분해, SnackBar, 코루틴 스코프
 */
@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun Test5() {
    val (text, setValue) = remember {
        mutableStateOf("")
    }

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold( // 머트리얼 디자인에서 스낵바등을 사용할때 사용
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField( // EditText
//            value = textValue.value,
//            onValueChange = {
//                textValue.value = it
//            })
                value = text,
                onValueChange = setValue,
            )
            Button(onClick = {
                keyboardController?.hide() // 키보드 숨김
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Hello $text") // suspend 함수
                }

            }) {
                Text("클릭!!")
            }
        }

    }

}

