// App.tsx
import { AppShell } from "@mantine/core";
import Header from "./header/header";
import Headlines from "./headlines/headlines";
import { useMediaQuery } from "@mantine/hooks";

const App: React.FC = () => {
    const isSmallScreen = useMediaQuery('(max-width: 768px)');
    const headerHeight = isSmallScreen ? 100 : 60; // adjust the values as needed

    return (
        <AppShell
            padding="md"
            header={<Header height={headerHeight} />}
        >
            <Headlines />
        </AppShell>
    );
};

export default App;