import {
    Button,
    Container,
    Group,
    Header as MantineHeader,
    NumberInput,
    Stack,
    Switch,
    Title,
    useMantineColorScheme,
} from "@mantine/core";
import {useStore} from "../store";
import {useMediaQuery} from "@mantine/hooks";

interface HeaderProps {
    height: number;
}

const Header = ({height}: HeaderProps) => {
    const {hours, setHours} = useStore();
    const {colorScheme, toggleColorScheme} = useMantineColorScheme();
    const dark = colorScheme === "dark";
    const isSmallScreen = useMediaQuery("(max-width: 768px)");
    const setHoursToLastDay = () => setHours(24);

    return (
        <MantineHeader height={height} p="sm">
            <Container>
                {isSmallScreen ? (
                    <Stack spacing="md">
                        <Group spacing="xs">
                            <Switch
                                checked={dark}
                                onChange={() => toggleColorScheme()}
                                size="lg"
                                onLabel="🌙"
                                offLabel="🌞"
                            />
                            <Title order={3}>My News</Title>
                        </Group>
                        <Group spacing="xs">
                            <NumberInput
                                size="sm"
                                value={hours}
                                onChange={(value) => setHours(value || 0)}
                                min={0}
                                max={168}
                                step={1}
                            />
                            <Title order={4}>Hours</Title>
                            <Button onClick={setHoursToLastDay}>Last Day</Button>
                        </Group>
                    </Stack>
                ) : (
                    <Group position="apart">
                        <Group spacing="xs">
                            <Switch
                                checked={dark}
                                onChange={() => toggleColorScheme()}
                                size="lg"
                                onLabel="🌙"
                                offLabel="🌞"
                            />
                            <Title order={3}>My News</Title>
                        </Group>
                        <Group spacing="xs">
                            <NumberInput
                                size="sm"
                                value={hours}
                                onChange={(value) => setHours(value || 0)}
                                min={0}
                                max={168}
                                step={1}
                            />
                            <Title order={4}>Hours</Title>
                            <Button onClick={setHoursToLastDay}>Last Day</Button>
                        </Group>
                    </Group>
                )}
            </Container>
        </MantineHeader>
    );
};

export default Header;