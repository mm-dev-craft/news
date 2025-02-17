import { useState, useEffect, useMemo } from "react";
import Headline from "./headline.tsx";
import { ScrollArea, Stack, Text, Group, Badge, SimpleGrid } from "@mantine/core";
import { useStore } from "../store";
import { getAllHeadlines } from "./headline.client.ts";
import { useMediaQuery } from "@mantine/hooks";

const Headlines = () => {
    const { hours, headlines, setHeadlines, selectedKeywords, toggleKeyword } = useStore();
    const isSmallScreen = useMediaQuery('(max-width: 768px)');
    const [selectedSources, setSelectedSources] = useState<string[]>([]);

    useEffect(() => {
        const fetchHeadlines = async () => {
            const loadedHeadlines = await getAllHeadlines(hours);
            setHeadlines(loadedHeadlines);
        };
        fetchHeadlines();
    }, [hours, setHeadlines]);

    const headlinesBySource = useMemo(() => {
        return headlines.reduce((acc, headline) => {
            acc[headline.source] = (acc[headline.source] || 0) + 1;
            return acc;
        }, {} as Record<string, number>);
    }, [headlines]);

    const sortedHeadlines = useMemo(() => {
        return [...headlines].sort(
            (a, b) => new Date(b.crawlingDate).getTime() - new Date(a.crawlingDate).getTime()
        );
    }, [headlines]);

    const filteredHeadlines = useMemo(() => {
        return sortedHeadlines.filter((headline) => {
            // If sources are selected, the headline's source must be one of them
            const sourceMatches = selectedSources.length === 0 || selectedSources.includes(headline.source);
            // If keywords are selected, the headline must include at least one of them
            const keywordMatches =
                selectedKeywords.length === 0 ||
                headline.keywords.some((keyword) => selectedKeywords.includes(keyword));
            return sourceMatches && keywordMatches;
        });
    }, [sortedHeadlines, selectedSources, selectedKeywords]);

    const toggleSourceSelection = (source: string) => {
        setSelectedSources((prevSelectedSources) =>
            prevSelectedSources.includes(source)
                ? prevSelectedSources.filter((s) => s !== source)
                : [...prevSelectedSources, source]
        );
    };

    return (
        <>
            <Stack spacing="sm" m={20}>
                {isSmallScreen ? (
                    Object.entries(headlinesBySource).map(([source, count]) => (
                        <Group key={source} position="apart" spacing={0}>
                            <Badge
                                color={selectedSources.includes(source) ? "green" : "blue"}
                                variant="filled"
                                size="lg"
                                onClick={() => toggleSourceSelection(source)}
                                style={{ cursor: "pointer" }}
                            >
                                {source}
                            </Badge>
                            <Text weight={500} size="lg">
                                {count} headline{count > 1 ? "s" : ""}
                            </Text>
                        </Group>
                    ))
                ) : (
                    <SimpleGrid cols={3} spacing="lg">
                        {Object.entries(headlinesBySource).map(([source, count]) => (
                            <Group key={source} position="center" spacing={10}>
                                <Badge
                                    color={selectedSources.includes(source) ? "green" : "blue"}
                                    variant="filled"
                                    size="lg"
                                    onClick={() => toggleSourceSelection(source)}
                                    style={{ cursor: "pointer" }}
                                >
                                    {source}
                                </Badge>
                                <Text weight={500} size="lg">
                                    {count} headline{count > 1 ? "s" : ""}
                                </Text>
                            </Group>
                        ))}
                    </SimpleGrid>
                )}
            </Stack>
            {selectedKeywords.length > 0 && (
                <Stack spacing="sm" m={20}>
                    <Group spacing="xs">
                        <Text weight={200} size="md">
                            Active Keywords:
                        </Text>
                        {selectedKeywords.map((keyword) => (
                            <Badge
                                key={keyword}
                                color="green"
                                variant="filled"
                                size="md"
                                style={{ cursor: "pointer" }}
                                onClick={() => toggleKeyword(keyword)}
                            >
                                {keyword} &times;
                            </Badge>
                        ))}
                    </Group>
                </Stack>
            )}

            <ScrollArea h={700}>
                <Stack spacing="sm" m={20}>
                    {filteredHeadlines.map((headline) => (
                        <Headline
                            key={headline.link}
                            crawlingDate={headline.crawlingDate}
                            link={headline.link}
                            source={headline.source}
                            title={headline.title}
                            keywords={headline.keywords}
                        />
                    ))}
                </Stack>
            </ScrollArea>
        </>
    );
};

export default Headlines;
