package com.xceptance.xrunner.groups;

import java.lang.annotation.Annotation;

public class GroupHelper
{
    public static TestGroups createDefaultGroups()
    {
        return wrapGroup(createDefaultGroup());
    }

    private static TestGroup createDefaultGroup()
    {
        return new TestGroup()
        {
            @Override
            public Class<? extends Annotation> annotationType()
            {
                return TestGroup.class;
            }

            @Override
            public int ordinal()
            {
                // as defined in the annotation
                return -1;
            }

            @Override
            public Class<?> group()
            {
                // as defined in the annotation
                return DefaultGroup.class;
            }
        };
    }

    public static TestGroups wrapGroup(TestGroup group)
    {
        TestGroups wrappedGroup = new TestGroups()
        {

            @Override
            public Class<? extends Annotation> annotationType()
            {
                return TestGroups.class;
            }

            @Override
            public TestGroup[] value()
            {
                TestGroup[] defaultGroups = new TestGroup[]
                    {
                        group
                    };
                return defaultGroups;
            }
        };

        return wrappedGroup;
    }
}
