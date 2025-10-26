import {AppShell} from "@mantine/core";
import Header from "./header/header";
import Headlines from "./headlines/headlines";
import {useMediaQuery} from "@mantine/hooks";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import SimiliarHeadlineList from "./similiar-headlines/similiar-headline-list.tsx";

const App: React.FC = () => {
    const isSmallScreen = useMediaQuery("(max-width: 768px)");
    const headerHeight = isSmallScreen ? 100 : 60;

    return (
        <BrowserRouter>
            <AppShell padding="md" header={<Header height={headerHeight}/>}>
                <Routes>
                    <Route path="/" element={<Headlines/>}/>
                    <Route path="/similiar-headlines" element={<SimiliarHeadlineList/>}/>
                </Routes>
            </AppShell>
        </BrowserRouter>
    );
};

export default App;