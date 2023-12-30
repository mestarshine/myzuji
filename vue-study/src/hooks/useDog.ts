
import {reactive, ref} from "vue";
import axios from "axios";

export default function (){
    let dogList = reactive(["https://images.dog.ceo/breeds/pembroke/n02113023_6567.jpg"]);

    async function getDog(){
        try {
            let result = await axios.get("https://dog.ceo/api/breed/pembroke/images/random");
            dogList.push(result.data.message)
        } catch (error){
            alert("请求失败"+ error);
        }
    }

    return {dogList,getDog}
}
