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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.psg.jetpackcomposepractice.ui.theme.JetpackComposePracticeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    //    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { // Compose가 시작되는 부분
            var isFavorite by rememberSaveable { // rememberSaveable 생명주기가 다시 돌아도 데이터를 보존
                mutableStateOf(false)
            }

            val viewModel = viewModel<MainViewModel>()

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

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        viewModel.data.value,
                        fontSize = 30.sp,
                    )
                    Button(onClick = {
                        viewModel.changeValue()
                    }) {
                        Text(text = "변경")

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
//@Preview
    @Composable
    fun Test5() {
        val (text, setValue) = remember {
            mutableStateOf("")
        }

        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()

        val keyboardController = LocalSoftwareKeyboardController.current // 키보드 컨트롤러 객체

        Scaffold( // 머트리얼 디자인에서 스낵바등을 사용할때 사용
            scaffoldState = scaffoldState
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TextField(
                    // EditText
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

    /**
     * Navigation
     */
//@Preview
    @Composable
    fun Test6() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "first"
        ) {
            composable("first") {
                FirstScreen(navController)
            }
            composable("second") {
                SecondScreen(navController)
            }
            composable("third/{value}") { backStackEntry -> // 넘겨받을 value
                ThirdScreen(
                    navController,
                    backStackEntry.arguments?.getString("value") ?: ""
                )
            }
        }

    }

    @Composable
    fun FirstScreen(navController: NavController) {
        val (value, setValue) = remember {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "첫 화면")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController.navigate("second") // 화면 전환
            }) {
                Text(text = "두 번째!")
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(value = value, onValueChange = setValue)
            Button(onClick = {
                if (value.isNotEmpty())
                    navController.navigate("third/$value")
            }) {
                Text(text = "세 번째!")
            }
        }
    }

    @Composable
    fun SecondScreen(navController: NavController) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "두 번째 화면")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController.navigateUp()
//            navController.popBackStack() // 위의 함수와 비슷한 동작작
            }) {
                Text(text = "뒤로 가기!")
            }
        }
    }

    @Composable
    fun ThirdScreen(navController: NavController, value: String) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "세 번째 화면")
            Spacer(modifier = Modifier.height(16.dp))
            Text(value)
            Button(onClick = {

                navController.navigateUp()
            }) {
                Text(text = "뒤로 가기!")
            }
        }
    }

    @Preview
    @Composable
    fun Test7() {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            Text(
////                viewModel.data.value,
//                fontSize = 30.sp,
//            )
            Button(onClick = {
//                viewModel.data.value = "World"
            }) {
                Text(text = "변경")

            }
        }

    }


    /**
     * State Deep
     */
    @Preview
    @Composable
    fun Test8(viewModel: MainViewModel = viewModel()) {

        var text1: MutableState<String> = remember {
            mutableStateOf("Hello World")
        }

        // by 사용시 String
        // Setter와 Getter재정의
        var text2:String by remember {
            mutableStateOf("Hello World")
        }

        // 화면이 다시 그려짐 - Recomposition
        val (text, setText: (String) -> Unit) = remember {
            mutableStateOf("Hello World")
        }

        // 기존의 LiveData 코드 사용시 LiveData를 State형태로 변환
        val text3: State<String> = viewModel.liveData.observeAsState("Hello World")

        Column() {
            Text("Hello World")
            Button(onClick = {
               text1.value = "변경"
                println(text1.value)
                text2 = "변경"
                println(text2)
                text = "변경" // val 타입이라 변경 x
                setText("변경") // setText함수로 변경
                viewModel.value.value = "변경" // readonly타입이라 변경x
                viewModel.changeValue2("변경") // 함수로 state안의 값을 변경

                text3.value = "변경" // readonly
                viewModel.changeValue3("변경")

            }) {
                Text(text = "클릭")
            }
        }
        TextField(value = text, onValueChange = setText)

    }


    class MainViewModel : ViewModel() {
        private val _data = mutableStateOf("Hello")
        val data: State<String> = _data

        fun changeValue() {
            _data.value = "World"

        }

        private val _value = mutableStateOf("Hello World")
        val value: State<String> = _value // read only

        private val _liveData = MutableLiveData<String>()
        val liveData: LiveData<String> = _liveData

        fun changeValue2(value: String) {
            _value.value = value
        }

        fun changeValue3(value: String) {
            _liveData.value = value
        }
    }

}

