package com.xceptance.xrunner.groups;

import java.lang.annotation.Annotation;
import java.util.List;

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
                TestGroup[] defaultGroups;
                if (group.group() == DefaultGroup.class)
                {
                    defaultGroups = new TestGroup[]
                        {
                            group
                        };
                }
                else
                {
                    defaultGroups = new TestGroup[]
                        {
                            createDefaultGroup(), group
                        };
                }
                return defaultGroups;
            }
        };

        return wrappedGroup;
    }

    public static TestGroups addDefaultGroupToTestGroups(TestGroups testGroups)
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
                for (int i = 0; i < testGroups.value().length; i++)
                {
                    if (testGroups.value()[i].group() == DefaultGroup.class)
                    {
                        return testGroups.value();
                    }
                }

                TestGroup[] defaultGroups = new TestGroup[testGroups.value().length + 1];
                defaultGroups[0] = createDefaultGroup();
                for (int i = 0; i < testGroups.value().length; i++)
                {
                    defaultGroups[i + 1] = testGroups.value()[i];
                }

                return defaultGroups;
            }
        };

        return wrappedGroup;
    }

    public static boolean testGroupMatch(TestGroups annotatedGroups, List<Class<?>> groupsToExecute, boolean matchAny)
    {
        // if not matchAny then it's matchAll

        boolean found;
        if (matchAny)
        {
            found = false;
        }
        else
        {
            found = true;
        }
        for (TestGroup annotatedGroup : annotatedGroups.value())
        {
            boolean executionGroupsContainsAnnotatedGroup = groupsToExecute.contains(annotatedGroup.group());
            if (matchAny)
            {
                found |= executionGroupsContainsAnnotatedGroup;
            }
            else
            {
                found &= executionGroupsContainsAnnotatedGroup;
            }
        }

        return found;
    }
}
