package com.huke.validation;

import com.alibaba.fastjson2.JSON;
import lombok.*;
import org.assertj.core.util.Lists;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author huke
 * @date 2022/08/31/上午11:22
 */

public class LambdaTest {

    public static void main(String[] args) {
        String[] str = {"hello", "world"};
        //1.需要按照字符保存到List<String> 输出helloworld
        List<String> res1 = Arrays.stream(str)
                .map(s -> s.split(""))
                .flatMap(Stream::of).collect(Collectors.toList());
        List<Integer> list1 = List.of(1, 2, 7, 9);
        List<Integer> list2 = List.of(8, 3, 6, 5);
        //2、合并两个集合 并排序,并且输出   key value 形式 ：[1，2，3，5]     [6，7，8，9]
        List<Integer> temp = Stream.concat(Stream.of(list1), Stream.of(list2))
                .collect(ArrayList<Integer>::new, ArrayList::addAll, ArrayList::addAll)
                .stream()
                .sorted().collect(Collectors.toList());

        List<List<Integer>> res2 = Stream.iterate(0, n -> n + 1)
                .limit((list1.size() + list2.size()) / 2)
                .parallel()
                .map(a -> temp.parallelStream().skip(a * 4).limit(4).collect(Collectors.toList()))
                .filter(b -> !b.isEmpty())
                .collect(Collectors.toList());

        List<String> list = Lists.newArrayList("zooor", "zoot", "", "serwe", "457");
        //找出集合 中匹配上 zoo的字符
        List<List<String>> zoo = Optional.of(list).stream()
                .map(it -> it.contains("zoo") ? it : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());



        List<Address> addressList = List.of(Address.builder().provinces("北京市").city("朝阳区"). street("高碑店").build(),
                Address.builder().provinces("北京市").city("朝阳区"). street("官庄").build(),
                Address.builder().provinces("北京市").city("万省区"). street("斯神殿").build(),
                Address.builder().provinces("北京市").city("昌平区"). street("方装点").build(),
                Address.builder().provinces("北京市").city("昌平区"). street("圆圆殿").build(),
                Address.builder().provinces("重庆市").city("万生区"). street("而起头").build(),
                Address.builder().provinces("重庆市").city("万生区"). street("而起头2").build(),
                Address.builder().provinces("重庆市").city("山水区"). street("二分店").build());
        //组织数结构返回
        List<Address> res = addressList.stream().collect(Collectors.groupingBy(Address::getProvinces))
                .entrySet().stream()
                .map(it -> {
                    Address one = Address.builder().provinces(it.getKey()).build();    //省份
                    it.getValue().stream().collect(Collectors.groupingBy(Address::getCity))
                            .forEach((key2, value2) -> {
                                Address two = Address.builder().city(key2).build();//区县
                                value2.stream().collect(Collectors.groupingBy(Address::getStreet))
                                        .forEach((key3, value3) -> {
                                            Address three = Address.builder(). street(key3).build();//街道
                                            two.add(three);
                                        });
                                one.add(two);
                            });
                   return one;
                }).collect(Collectors.toList());

        System.out.println(JSON.toJSONString(res));


    }

}

@Data
@NonNull
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Address {
    private String provinces; //省份
    private String city; //城市
    private String  street; //街道
    private List<Address> childAddress;

    public void add(Address address) {
        if (Objects.isNull(address)) {
            return;
        }
        if (Objects.isNull(childAddress)) {
            childAddress = Lists.newArrayList();
        }
        childAddress.add(address)
        ;
    }
}

