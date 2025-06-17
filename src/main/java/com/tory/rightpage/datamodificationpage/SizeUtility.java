package com.tory.rightpage.datamodificationpage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

public final class SizeUtility
{
	public static <T extends JPanel> int getHugSize(T panel)
	{
		int size = 0;
		
		// panel의 멤버 변수들 중 JComponent 자료형을 가지는 멤버 변수들의 getHeight 함수를 각각 호출해서 모두 반환
		List<Object> heights = invokeMethodOnMatchingFields(panel, JComponent.class, "getHeight");
		
		// heights에서 가장 큰 값을 size에 넣는 반복문
		for(int i = 0; i < heights.size(); i++)
		{
			int height = (int)heights.get(i);
			
			size = size < height ? height : size;
		}
		
		return size;
	}
	// obj의 멤버 변수들 중 targetType 자료형인 멤버 변수들에게 methodName과 이름이 같은 본인들의 멤버 함수를 호출.
	private static List<Object> invokeMethodOnMatchingFields(Object obj, Class<?> targetType, String methodName)
	{
        Class<?> clazz = obj.getClass();
        List<Object> returnValues = new ArrayList<Object>();

        for (Field field : clazz.getDeclaredFields())
        {
            field.setAccessible(true); // private 필드 접근 허용

            // 필드 타입이 targetType의 하위 클래스 or 구현체인지 확인
            if (targetType.isAssignableFrom(field.getType()))
            {
                try
                {
                    Object fieldValue = field.get(obj); // 필드 값 (인스턴스)
                    
                    if (fieldValue != null)
                    {
                        // 지정된 메서드 얻기 (파라미터 없는 경우)
                        Method method = fieldValue.getClass().getMethod(methodName);
                        returnValues.add(method.invoke(fieldValue)); // 메서드 호출
                    }
                }
                catch (NoSuchMethodException e)
                {
                    System.out.println("Method not found in " + field.getName());
                }
                catch (IllegalAccessException | InvocationTargetException e)
                {
                    e.printStackTrace();
                }
            }
        }
        
        return returnValues;
    }
}