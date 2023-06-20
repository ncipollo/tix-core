# Links 

- Rest API: https://docs.github.com/en/rest/quickstart?apiVersion=2022-11-28
- Issues: https://docs.github.com/en/rest/issues/issues?apiVersion=2022-11-28

# Sample Graph QL

## Create Issue

```graphql
mutation CreateIssue {
  createIssue(input: {
    repositoryId: "MDEwOlJlcG9zaXRvcnkzMzY5MjAyMzQ="
    
    title: "GraphQL Test Issue"
    body: """```kotlin
    println("test")
    \"""
    thing
    \"""
    ```
    **This is a test**
    - Item 1
    - Item 2
    - Item 3
    
    `code snippet`
    
    [Duck Duck Go](https://duckduckgo.com)
    
    ----
    
    - [ ] task 1
    - [ ] task 2
    """
  }) {
    issue {
      id
      number
    }
  }
}
```

## Get Milestones

```graphql
query GetMilestone($pageCursor: String) {
  repository(owner: "ncipollo", name: "tix-core") {
    milestones(after: $pageCursor, first: 50) {
      nodes {
        id
        number
        title
        state
      }
      pageInfo {
        hasNextPage
        endCursor
      }
    }
  }
}
```

## Get Repo Issue

```graphql
query GetIssue($pageCursor: String, $owner: String!, $repo: String!) {
  repository(owner: $owner, name: $repo) {
    content: issues(after: $pageCursor, first: 50) {
      pageInfo {
        hasNextPage
        endCursor
      }
      nodes {
        id
        number
        title
        body
        state
        author {
          login
        }
        assignees(first: 20) {
          nodes {
            id
            name
            login
          }
        }
        milestone {
          id
          title
          number
        }
        labels(first: 20) {
          nodes {
            id
            name
          }
        }
      }
    }
  }
  rateLimit {
    limit
    cost
    remaining
    resetAt
  }
}
```

## Scratch

```json
{"data":{"createProjectV2":{"content":{"id":"PVT_kwHOAFcrdM4ASuiG","number":6,"title":"Test Project","shortDescription":null,"closed":false}}},"extensions":{"warnings":[{"type":"DEPRECATION","message":"The id MDQ6VXNlcjU3MTI3NTY= is deprecated. Update your cache to use the next_global_id from the data payload.","data":{"legacy_global_id":"MDQ6VXNlcjU3MTI3NTY=","next_global_id":"U_kgDOAFcrdA"},"link":"https://docs.github.com"},{"type":"DEPRECATION","message":"The id MDEwOlJlcG9zaXRvcnkzMzY5MjAyMzQ= is deprecated. Update your cache to use the next_global_id from the data payload.","data":{"legacy_global_id":"MDEwOlJlcG9zaXRvcnkzMzY5MjAyMzQ=","next_global_id":"R_kgDOFBT-qg"},"link":"https://docs.github.com"}]}}
```